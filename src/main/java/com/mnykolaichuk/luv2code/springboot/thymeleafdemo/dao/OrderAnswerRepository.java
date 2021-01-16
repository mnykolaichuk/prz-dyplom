package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.OrderAnswer;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderAnswerRepository extends JpaRepository<OrderAnswer, Integer> {
    public List<OrderAnswer> findAllOrderAnswerByWorkshops(Workshop workshop);
    public OrderAnswer findOrderAnswerById(Integer id);
}
