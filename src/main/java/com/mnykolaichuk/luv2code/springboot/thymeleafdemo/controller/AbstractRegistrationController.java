package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Authority;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.AuthorityService;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public abstract class AbstractRegistrationController {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private CityService cityService;

    protected List<String> cities = new ArrayList<>();
    protected Map<String, String> roles;

    @PostConstruct
    private void loadRoles() {
        roles = new LinkedHashMap<String, String>();
        String tempValue;
        for(Authority authority : authorityService.findAll()){
            tempValue = authority.getAuthority();
            tempValue = tempValue.substring(5);
            tempValue = tempValue.toLowerCase();
            roles.put(authority.getAuthority(), tempValue);
            tempValue = null;
        }
    }

    @PostConstruct
    private void loadCities() {
        cities = cityService.loadCites();
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

}
