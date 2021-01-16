package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.EmployeeCarRepositoryCustom;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.EmployeeCar;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class EmployeeCarRepositoryCustomImpl implements EmployeeCarRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<EmployeeCar> findEmployeeCarByCarId(int id) {
        Session currentSession = entityManager.unwrap(Session.class);

        // now retrieve/read from database using name
        Query<EmployeeCar> theQuery = currentSession.createQuery("SELECT ec from EmployeeCar ec " +
                "where ec.carId =: carId");

        theQuery.setParameter("carId", id);
        List<EmployeeCar> employeeCars = null;
        try {
            employeeCars = theQuery.getResultList();
        } catch (Exception e) {
            employeeCars = null;
            e.printStackTrace();
        }
        return employeeCars;
    }

}

