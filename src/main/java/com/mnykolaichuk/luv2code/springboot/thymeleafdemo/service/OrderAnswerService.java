package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.OrderAnswer;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.OrderAnswerData;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderAnswerService {
    public List<OrderAnswer> findAllByWorkshopAndStanEquals(Workshop workshop, Stan stan);
    public List<OrderAnswer> findAllByUsernameAndStanEqualsCreated(String username);
    public OrderAnswer findById(Integer id);
    public void createWorkshopRepeatOrderAnswer(OrderAnswerData orderAnswerData);
    public void sendInformationEmail(Integer orderAnswerId);
//    public void createImplementationOrderAnswer(OrderAnswerData orderAnswerData);
}
