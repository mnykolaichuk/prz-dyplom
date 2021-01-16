package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.OrderAnswerRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.OrderRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.Stan;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.email.InformationEmailContext;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Order;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.OrderAnswer;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.CarData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.OrderData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private EmployeeDetailService employeeDetailService;
    @Autowired
    private CityService cityService;
    @Autowired
    private WorkshopService workshopService;
    @Autowired
    private CarService carService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderAnswerRepository orderAnswerRepository;

    @Autowired
    private OrderAnswerService orderAnswerService;

    private final String SHOW_DETAILS_URL="localhost:8080";

    @Override
    public void createOrder(CarData carData, String username, OrderData orderData) {
        Order order = new Order();
        order.setDescription(orderData.getDescription());
        order.setCreationDate(orderData.getCreationDate());
        order.setEmployeeDetail(employeeDetailService.findByUsername(username));
        order.setCity(cityService.findByCityName(orderData.getCityName()));
        order.setCar(carService.saveForOrder(carData));
        orderRepository.save(order);
        for(Workshop workshop : workshopService.findAllByCity(order.getCity())) {
            OrderAnswer tempOrderAnswer = new OrderAnswer();
            tempOrderAnswer.setStan(Stan.CREATED);
            tempOrderAnswer.setWorkshops(Arrays.asList(workshop));
            tempOrderAnswer.setOrder(order);
            orderAnswerRepository.save(tempOrderAnswer);
        }
        sendInformationEmails(workshopService.findAllByCity(order.getCity()));
    }

    @Override
    public void sendInformationEmails(List<Workshop> workshops) {
        InformationEmailContext emailContext = new InformationEmailContext();
        for(Workshop workshop : workshops) {
            emailContext.init(workshop);
            emailContext.setInformationUrl(SHOW_DETAILS_URL);
            try {
                emailService.sendInformationMail(emailContext);
            }
            catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Order> findAllByOrderAnswers(List<OrderAnswer> orderAnswers) {
        List<Order> tempOrders = new ArrayList<>();
        for(OrderAnswer orderAnswer : orderAnswers) {
            tempOrders.add(orderAnswer.getOrder());
        }
        return tempOrders;
    }


    @Override
    public List<Order> findAllByUsernameAndStanEqualsCreated(String username) {
        return findAllByOrderAnswers(orderAnswerService.findAllByUsernameAndStanEqualsCreated(username));
    }

    @Override
    public List<OrderData> getOrderDataListByUsernameAndStanEqualsCreated(String username) {
        List<OrderData> orderDataList = new ArrayList<>();
        for(Order order : findAllByUsernameAndStanEqualsCreated(username)){
            OrderData tempOrderData = new OrderData();
            for(OrderAnswer orderAnswer : order.getOrderAnswers()) {
                for(Workshop workshop : orderAnswer.getWorkshops()) {
                    if(workshop.getUsername().equals(username)) {
                        tempOrderData.setOrderAnswerId(orderAnswer.getId());
                    }
                }
            }
            tempOrderData.setDescription(order.getDescription());
            tempOrderData.setCreationDate(order.getCreationDate());
            tempOrderData.setCityName(order.getCity().getCityName());
            tempOrderData.setEmployeeDetailData
                    (employeeDetailService.getEmployeeDetailData(order.getEmployeeDetail()));
            tempOrderData.setCarData(carService.getCarData(order.getCar()));
            orderDataList.add(tempOrderData);
        }
        return orderDataList;
    }

    @Override
    public List<OrderData> getOrderDataListByUsernameAndStanEqualsWorkshopRepeat(String username) {
        List<OrderData> orderDataList = new ArrayList<>();
        for(Order order : findAllByUsernameAndStanEqualsCreated(username)){
            OrderData tempOrderData = new OrderData();
            for(OrderAnswer orderAnswer : order.getOrderAnswers()) {
                for(Workshop workshop : orderAnswer.getWorkshops()) {
                    if(workshop.getUsername().equals(username)) {
                        tempOrderData.setOrderAnswerId(orderAnswer.getId());
                    }
                }
            }
            tempOrderData.setDescription(order.getDescription());
            tempOrderData.setCreationDate(order.getCreationDate());
            tempOrderData.setCityName(order.getCity().getCityName());
            tempOrderData.setEmployeeDetailData
                    (employeeDetailService.getEmployeeDetailData(order.getEmployeeDetail()));
            tempOrderData.setCarData(carService.getCarData(order.getCar()));
            orderDataList.add(tempOrderData);
        }
        return orderDataList;
    }

    @Override
    public Order findOrderById(Integer id) {
        return orderRepository.findOrderById(id);
    }
}
