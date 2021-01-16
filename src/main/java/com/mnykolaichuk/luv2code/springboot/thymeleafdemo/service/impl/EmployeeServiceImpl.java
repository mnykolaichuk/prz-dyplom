package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.*;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.EmailAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.InvalidTokenException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.UserAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.WrapperString;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.email.AccountVerificationEmailContext;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Employee;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.EmployeeDetail;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.SecureToken;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.EmployeeData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.EmailService;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.SecureTokenService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDetailRepository employeeDetailRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SecureTokenService secureTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeCarRepository employeeCarRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Value("${site.base.url.http}")
    private String baseURL;

    private final String ROLE_EMPLOYEE = "ROLE_EMPLOYEE";

    @Override
    public void register(EmployeeData employeeData) throws UserAlreadyExistException, EmailAlreadyExistException {
        if(checkIfUserExist(employeeData.getUsername())) {
            employeeData.setUsername(null);
            throw new UserAlreadyExistException("User already exist");
        }
        if(checkIfEmailExist(employeeData.getEmail())) {
            //перевірка для того якщо юзер створив ордер не запеєстрований, він є в базі. хоче зареєструватися. щоб міг.
            if(employeeDetailRepository.findByEmail(employeeData.getEmail()).getEmployee() != null) {
                employeeData.setEmail(null);
                throw new EmailAlreadyExistException("Email address already used");
            }
        }
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeData, employee);
        EmployeeDetail employeeDetail = new EmployeeDetail();
        BeanUtils.copyProperties(employeeData, employeeDetail);
        encodePassword(employeeData, employee);
        employee.setAuthorities(Arrays.asList(authorityRepository.findByAuthority(ROLE_EMPLOYEE)));
        employeeDetail.setEmployee(employee);
        employeeDetailRepository.save(employeeDetail);
        sendRegistrationConfirmationEmail(employeeDetail);
    }



    private void encodePassword(EmployeeData source, Employee target) {
        target.setPassword(passwordEncoder.encode(source.getPassword()));
    }

    @Override
    public boolean verifyEmployee(String token) throws InvalidTokenException {
        SecureToken secureToken = secureTokenService.findByToken(token);
        if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
            throw new InvalidTokenException("Token is not valid");
        }
        EmployeeDetail employeeDetail = employeeDetailRepository.getOne(secureToken.getEmployeeDetail().getId());
        if(Objects.isNull(employeeDetail)){
            return false;
        }
        employeeDetail.setAccountVerified(true);
        employeeDetailRepository.save(employeeDetail); // let's same user details

        // we don't need invalid password now
        secureTokenService.removeToken(secureToken);
        return true;
    }

    @Override
    public void update(EmployeeData employeeData, WrapperString wrapperString)
            throws UserAlreadyExistException, EmailAlreadyExistException {
        boolean isEmailChange = false;
        boolean isUsernameChange = false;
        if(!employeeData.getUsername().equals(wrapperString.getOldUsername())) {
            isUsernameChange = true;
            if (checkIfUserExist(employeeData.getUsername())) {
                employeeData.setUsername(null);
                throw new UserAlreadyExistException("User already exist");
            }
        }
        if(!employeeData.getEmail().equals(wrapperString.getOldEmail())) {
            isEmailChange = true;
            if (checkIfEmailExist(employeeData.getEmail())) {
                employeeData.setEmail(null);
                throw new EmailAlreadyExistException("Email address already used");
            }
        }
        if(isUsernameChange) {
            Employee employee = employeeRepository.findByUsername(wrapperString.getOldUsername());
            employee.setUsername(employeeData.getUsername());
            employeeRepository.save(employee);
        }
        EmployeeDetail employeeDetail =
                employeeRepository.findByUsername(wrapperString.getOldUsername()).getEmployeeDetail();
        BeanUtils.copyProperties(employeeData, employeeDetail);
        if(isEmailChange) {
            employeeDetail.setAccountVerified(false);
            employeeDetailRepository.save(employeeDetail);
            sendRegistrationConfirmationEmail(employeeDetail);
        }
        else {
            employeeDetailRepository.save(employeeDetail);
        }
    }




    @Override
    public boolean comparePassword(String password, String encodePassword) {

        passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatches = passwordEncoder.matches(password, encodePassword);
        if (isPasswordMatches)
            return true;
        return false;
    }



    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public EmployeeData getEmployeeDataByUsername(String username) {
        EmployeeData employeeData =
                copyProperties(findByUsername(username), findByUsername(username).getEmployeeDetail());
        return employeeData;
    }

    private EmployeeData copyProperties(Employee employee, EmployeeDetail employeeDetail) {
        System.out.println(employee.getUsername());
        EmployeeData employeeData = new EmployeeData();
        employeeData.setUsername(employee.getUsername());
        System.out.println(employeeData.getUsername());
        employeeData.setPassword(employee.getPassword());
        employeeData.setMatchingPassword(employee.getPassword());
        employeeData.setFirstName(employeeDetail.getFirstName());
        employeeData.setLastName(employeeDetail.getLastName());
        employeeData.setEmail(employeeDetail.getEmail());
        employeeData.setPhoneNumber(employeeDetail.getPhoneNumber());

        return employeeData;
    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    @Override
    public Employee findById(Integer id) {
        return employeeRepository.findEmployeeById(id);
    }

    @Override
    public void sendRegistrationConfirmationEmail(EmployeeDetail employeeDetail) {

        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                sendEmail(employeeDetail);
            }
        });
    }



    private void sendEmail(EmployeeDetail employeeDetail) {
        SecureToken secureToken = secureTokenService.createSecureToken();
        secureToken.setEmployeeDetail(employeeDetail);
        secureTokenService.saveSecureToken(secureToken);
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(employeeDetail);
        emailContext.setToken(secureToken.getToken());
        emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
        try {
            emailService.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfUserExist(String username) {
        return employeeRepository.findByUsername(username) != null ? true : false;
    }

    private boolean checkIfEmailExist(String email) {
        return employeeRepository.findByEmployeeDetailEmail(email) != null ? true : false;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
