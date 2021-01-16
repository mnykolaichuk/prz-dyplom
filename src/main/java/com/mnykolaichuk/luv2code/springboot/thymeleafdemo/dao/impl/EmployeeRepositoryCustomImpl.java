package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.EmployeeCarRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.EmployeeRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class EmployeeRepositoryCustomImpl implements EmployeeRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EmployeeCarRepository employeeCarRepository;

    //place for implementation of custom DAO methods


//    @Override
//    @Transactional
//    public List<Car> findAllCarsByUserName(String userName) {
//        Session currentSession = entityManager.unwrap(Session.class);
//
//        // now retrieve/read from database using name
//        Query<Car> theQuery = currentSession.createQuery("SELECT c from EmployeeCar ec, Employee e join e.cars c" +
//                " where e.username =: userName");
//
//        theQuery.setParameter("userName", userName);
//
//        List<Car> cars = null;
//
//        try {
//            cars = theQuery.getResultList();
//        } catch (Exception e) {
//            cars = null;
//        }
//        return cars;
//    }

//    @Override
//    @Transactional
//    public Employee findByEmail(String email) {
//        Session currentSession = entityManager.unwrap(Session.class);
//        // now retrieve/read from database using name
//        Query<Employee> theQuery = currentSession.createQuery("select e from Employee e" +
//                " where e.email = :email");
//
//        theQuery.setParameter("email", email);
//
//        Employee employee = null;
//
//        try {
//            employee = theQuery.setMaxResults(1).getSingleResult();
//        } catch (Exception e) {
//            employee = null;
//        }
//        return employee;
//    }
}

