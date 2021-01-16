package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller.employee;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller.AbstractRegistrationController;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.EmailAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.UserAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.EmployeeData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/register/employee")
public class EmployeeRegistrationController extends AbstractRegistrationController {

    @Autowired
    private EmployeeService employeeService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model theModel) {
		theModel.addAttribute("employeeData", new EmployeeData());
		return "employee/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public ModelAndView processRegistrationForm(
				@Valid @ModelAttribute("employeeData") EmployeeData employeeData,
				BindingResult bindingResult,
				Model model) {

		ModelAndView modelAndView = new ModelAndView();
		// form validation
		 if (bindingResult.hasErrors()){
		 	model.addAttribute("employeeData", employeeData);
		 	modelAndView.setViewName("employee/registration-form");
		 	return modelAndView;
		 }

       try {
		   employeeService.register(employeeData);
	   }
       catch (UserAlreadyExistException | EmailAlreadyExistException e){
		   modelAndView.addObject("employeeData", employeeData);
		   modelAndView.addObject("registrationError", e.getMessage());
		   modelAndView.setViewName("employee/registration-form");
		   return modelAndView;
        }
       modelAndView.addObject("username", employeeData.getUsername());
       modelAndView.addObject("email", employeeData.getEmail());
       modelAndView.setViewName("registration-successful");
        return modelAndView;
	}


}
