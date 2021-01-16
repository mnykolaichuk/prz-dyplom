package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Order;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.OrderAnswer;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.CarData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.OrderData;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    public void createOrder(CarData carData, String username, OrderData orderData);
    public void sendInformationEmails(List<Workshop> workshops);
    public List<Order> findAllByOrderAnswers(List<OrderAnswer> orderAnswers);
    public List<Order> findAllByUsernameAndStanEqualsCreated(String username);
    public List<OrderData> getOrderDataListByUsernameAndStanEqualsCreated(String username);
    public List<OrderData> getOrderDataListByUsernameAndStanEqualsWorkshopRepeat(String username);
    public Order findOrderById(Integer id);






}
