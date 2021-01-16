package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.AuthorityRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.WorkshopRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.EmailAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.InvalidTokenException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.UserAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.WrapperString;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.email.AccountVerificationEmailContext;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.City;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Order;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.SecureToken;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.WorkshopData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WorkshopServiceImpl implements WorkshopService {

    @Autowired
    private WorkshopRepository workshopRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

    @Value("${site.base.url.http}")
    private String baseURL;

    private final String ROLE_WORKSHOP = "ROLE_WORKSHOP";

    @Override
    public void register(WorkshopData workshopData) throws UserAlreadyExistException, EmailAlreadyExistException {
        if(checkIfUsernameExist(workshopData.getUsername())) {
            workshopData.setUsername(null);
            throw new UserAlreadyExistException("User already exist");
        }
        if(checkIfEmailExist(workshopData.getEmail())) {
            workshopData.setEmail(null);
            throw new EmailAlreadyExistException("Email address already used");
        }
        Workshop workshop = new Workshop();
        BeanUtils.copyProperties(workshopData, workshop);
        encodePassword(workshopData, workshop);
        workshop.setAuthorities(Stream.of
                (authorityRepository.findByAuthority(ROLE_WORKSHOP)).collect(Collectors.toSet()));
        workshop.setCity(cityService.findByCityName(workshopData.getCityName()));
        workshopRepository.save(workshop);
        sendRegistrationConfirmationEmail(workshop);

    }

    private void encodePassword(WorkshopData source, Workshop target) {
        target.setPassword(passwordEncoder.encode(source.getPassword()));
    }

    @Override
    public boolean checkIfUsernameExist(String username) {
        return workshopRepository.findByUsername(username) != null ? true : false;
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        return workshopRepository.findByEmail(email) != null ? true : false;
    }

    @Override
    public void sendRegistrationConfirmationEmail(Workshop workshop) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setWorkshop(workshop);
        secureTokenService.saveSecureToken(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(workshop);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean verifyWorkshop(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        Workshop workshop = workshopRepository.getOne(secureToken.getWorkshop().getId());
        if(Objects.isNull(workshop)){
            return false;
        }
        workshop.setAccountVerified(true);
        workshopRepository.save(workshop); // let's same user details

        // we don't need invalid password now
        secureTokenService.removeToken(secureToken);
        return true;
    }

    @Override
    public void update(WorkshopData workshopData, WrapperString wrapperString) throws UserAlreadyExistException, EmailAlreadyExistException {
        boolean isEmailChange = false;
        if (!workshopData.getUsername().equals(wrapperString.getOldUsername())) {
            if (checkIfUsernameExist(workshopData.getUsername())) {
                workshopData.setUsername(null);
                throw new UserAlreadyExistException("Workshop already exist");
            }
        }
        if (!workshopData.getEmail().equals(wrapperString.getOldEmail())) {
            isEmailChange = true;
            if (checkIfEmailExist(workshopData.getEmail())) {
                workshopData.setEmail(null);
                throw new EmailAlreadyExistException("Email address already used");
            }
        }

        Workshop workshop = new Workshop();
        workshop.setId(workshopRepository.findByUsername(wrapperString.getOldUsername()).getId());
        BeanUtils.copyProperties(workshopData, workshop);
        workshop.setCity(cityService.findByCityName(workshopData.getCityName()));
        if (isEmailChange) {
            workshop.setAccountVerified(false);
            workshopRepository.save(workshop);
            sendRegistrationConfirmationEmail(workshop);
        } else {
            workshopRepository.save(workshop);
        }
    }

    @Override
    public Workshop findByUsername(String username) {
        return workshopRepository.findByUsername(username);
    }

    @Override
    public Workshop findByWorkshopName(String workshopName) {
        return workshopRepository.findByWorkshopName(workshopName);
    }

    @Override
    public WorkshopData getWorkshopData(Workshop workshop) {
        WorkshopData workshopData = new WorkshopData();
        BeanUtils.copyProperties(workshop, workshopData);
        workshopData.setMatchingPassword(workshop.getPassword());

        return workshopData;
    }

    @Override
    public void deleteById(int id) {
        workshopRepository.deleteById(id);
    }

    @Override
    public List<Workshop> findAllByCity(City city) {
        return workshopRepository.findAllByCity(city);
    }

}
