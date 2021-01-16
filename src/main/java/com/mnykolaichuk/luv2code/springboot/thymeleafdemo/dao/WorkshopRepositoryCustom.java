package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.dao;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;

public interface WorkshopRepositoryCustom {
    //place for customs methods
    public void saveWithRepairType(Workshop workshop);
}
