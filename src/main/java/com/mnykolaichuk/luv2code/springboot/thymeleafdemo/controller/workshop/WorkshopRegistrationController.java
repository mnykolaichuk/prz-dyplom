package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller.workshop;

import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller.AbstractRegistrationController;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.EmailAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.exception.UserAlreadyExistException;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entityData.WorkshopData;
import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.WorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/register/workshop")
public class WorkshopRegistrationController extends AbstractRegistrationController {

    @Autowired
    private WorkshopService workshopService;

	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model model) {

		model.addAttribute("workshopData", new WorkshopData());
		model.addAttribute("cities", cities);
		return "workshop/registration-form";
	}

	@PostMapping("/processRegistrationForm")
	public ModelAndView processRegistrationForm(
				@Valid @ModelAttribute("workshopData") WorkshopData workshopData,
				BindingResult bindingResult,
				Model model) {

		ModelAndView modelAndView = new ModelAndView();
		// form validation
		 if (bindingResult.hasErrors()){
			 modelAndView.addObject("workshopData", workshopData);
			 modelAndView.addObject("cities", cities);
			 modelAndView.setViewName("workshop/registration-form");
			 return modelAndView;
	        }

		try {
			workshopService.register(workshopData);
		}
		catch (UserAlreadyExistException | EmailAlreadyExistException e){
			modelAndView.addObject("workshopData", workshopData);
			modelAndView.addObject("cities", cities);
			modelAndView.addObject("registrationError", e.getMessage());
			modelAndView.setViewName("workshop/registration-form");
			return modelAndView;
		}
		modelAndView.addObject("username", workshopData.getUsername());
		modelAndView.addObject("email", workshopData.getEmail());
		modelAndView.setViewName("registration-successful");
		return modelAndView;
	}

}
