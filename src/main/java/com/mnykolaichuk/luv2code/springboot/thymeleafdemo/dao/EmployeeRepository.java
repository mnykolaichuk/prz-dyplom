package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>, EmployeeRepositoryCustom{

    public Employee findByUsername(String username);
    public Employee findEmployeeById(Integer id);
    public Employee findByEmployeeDetailEmail(String email);

}
