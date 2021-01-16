package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Authority;

import java.util.List;

public interface AuthorityService {
    public List<Authority> findAll();
}
