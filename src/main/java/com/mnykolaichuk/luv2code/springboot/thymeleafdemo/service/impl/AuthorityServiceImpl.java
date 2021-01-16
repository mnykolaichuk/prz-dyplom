package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.impl;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao.AuthorityRepository;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Authority;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<Authority> findAll() {

        return authorityRepository.findAll();
    }
}
