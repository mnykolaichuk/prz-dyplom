package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.OrderAnswerRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.email.InformationEmailContext;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.EmployeeDetail;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.OrderAnswer;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.OrderAnswerData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.EmailService;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.OrderAnswerService;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderAnswerServiceImpl implements OrderAnswerService {

    @Autowired
    private OrderAnswerRepository orderAnswerRepository;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private EmailService emailService;


    private final String SHOW_DETAILS_URL="localhost:8080";

    @Override
    public List<OrderAnswer> findAllByWorkshopAndStanEquals(Workshop workshop, Stan stan) {
        List<OrderAnswer> orderAnswersWithStanEquals = new ArrayList<>();
        List<OrderAnswer> orderAnswers = orderAnswerRepository.findAllOrderAnswerByWorkshops(workshop);
        for (OrderAnswer orderAnswer : orderAnswers) {
            if(orderAnswer.getStan() == stan){
                orderAnswersWithStanEquals.add(orderAnswer);
            }
        }
        return orderAnswersWithStanEquals;
    }

    @Override
    public List<OrderAnswer> findAllByUsernameAndStanEqualsCreated(String username) {
        return findAllByWorkshopAndStanEquals(workshopService.findByUsername(username), Stan.CREATED);
    }

    @Override
    public OrderAnswer findById(Integer id) {
        return orderAnswerRepository.findOrderAnswerById(id);
    }

    @Override
    public void createWorkshopRepeatOrderAnswer(OrderAnswerData orderAnswerData) {
        OrderAnswer orderAnswer = findById(orderAnswerData.getId());
        orderAnswer.setImplementationDate(orderAnswerData.getImplementationDate());
        orderAnswer.setPrice(orderAnswerData.getPrice());
        orderAnswer.setStan(Stan.WORKSHOP_REPEAT);
        orderAnswerRepository.save(orderAnswer);
        sendInformationEmail(orderAnswerData.getId());
    }

    @Override
    public void sendInformationEmail(Integer orderAnswerId) {
        EmployeeDetail employeeDetail =
                orderAnswerRepository.findOrderAnswerById(orderAnswerId).getOrder().getEmployeeDetail();
        InformationEmailContext emailContext = new InformationEmailContext();
        emailContext.init(employeeDetail);
        emailContext.setInformationUrl(SHOW_DETAILS_URL);
        try {
            emailService.sendInformationMail(emailContext);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
