package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.EmailAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.InvalidTokenException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.UserAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.WrapperString;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.City;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.WorkshopData;

import java.util.List;
public interface WorkshopService {

    public void register(WorkshopData workshopData) throws UserAlreadyExistException, EmailAlreadyExistException;
    public boolean checkIfUsernameExist(String username);
    public boolean checkIfEmailExist(String email);
    public void sendRegistrationConfirmationEmail(Workshop workshop);
    public boolean verifyWorkshop(String token) throws InvalidTokenException;
    public Workshop findByUsername(String username);
    public Workshop findByWorkshopName(String workshopName);
    public WorkshopData getWorkshopData(Workshop workshop);
    public void update(WorkshopData workshopData, WrapperString wrapperString)
        throws UserAlreadyExistException, EmailAlreadyExistException;
    public void deleteById(int id);
    public List<Workshop> findAllByCity(City city);

}
