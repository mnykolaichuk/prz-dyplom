//package com.mnykolaichuk.luv2code.springboot.thymeleafdemo.controller.workshop;
//
//import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.entity.Workshop;
//import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.model.workshop.CrmRepairType;
//import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.RepairTypeService;
//import com.mnykolaichuk.luv2code.springboot.thymeleafdemo.service.WorkshopService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.CurrentSecurityContext;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/workshop")
//public class WorkshopController {
//
//    @Autowired
//    private WorkshopService workshopService;
//
//    @Autowired
//    private RepairTypeService repairTypeService;
//
//    private List<String> repairTypes = new ArrayList<>();
//
//    @PostConstruct
//    protected void loadRepairTypes() {
//
//        for(GlobalRepairType globalRepairType : repairTypeService.findAll()){
//            repairTypes.add(globalRepairType.getRepairTypeName());
//        }
//    }
//
//    @GetMapping("/dashboard")
//    public String showWorkshopDashboard(Model model
//            , @CurrentSecurityContext(expression = "authentication.name") String workshopName) {
//        Workshop workshop = new Workshop();
//        workshop = workshopService.findByWorkshopName(workshopName);
//        model.addAttribute("loginedWorkshop", workshop);
//        return "workshop/dashboard";
//    }
//
//    @GetMapping("/showAddRepairTypeForm")
//    public String showAddRepairTypeForm(Model model) {
//        model.addAttribute("repairTypes", repairTypes);
//        CrmRepairType crmRepairType = new CrmRepairType();
//        model.addAttribute("crmRepairType", crmRepairType);
//        GlobalRepairType globalRepairType = new GlobalRepairType();
//        model.addAttribute("globalRepairType", globalRepairType);
//        LocalRepairType localRepairType = new LocalRepairType();
//        model.addAttribute("localRepairType", localRepairType);
//        return "workshop/add-repair-type-form";
//    }
//
//    @PostMapping("/processAddRepairType")
//    public String processAddRepairType(@ModelAttribute("crmRepairType") CrmRepairType crmRepairType
//            , BindingResult bindingResult
//            , Model model
//            , @CurrentSecurityContext(expression = "authentication.name") String workshopName) {
//
//        //form validation
//        if(bindingResult.hasErrors()) {
//            return "employee/add-car-form";
//        }
//
//        Workshop workshop = workshopService.findByWorkshopName(workshopName);
//
//        System.out.println("logined workshopName" + workshopName);
//        //check the database if car with this registration number already exists
//        //ми маємо дивитися чи для даного воркшопу існує локал репаір тип з таким описом
//        LocalRepairType existing = null;
//        for(LocalRepairType lrt : workshopService.localRepairTypesForWorkshop(workshop.getId())) {
//            System.out.println("всі lrt для залогованого воркшопу" + lrt);
//            if(lrt.getDescription().equals(crmRepairType.getDescription()))
//                existing = lrt;
//        }
//
//        if(existing != null){
//            // To DO this сценарій
//            System.out.println("ми знайшли співпадіння");
////            model.addAttribute("car", new Car());
////            model.addAttribute("addInformation", "Car with this registration number already exist");
//
//            //TO DO треба написати форму для редавгування машини так як на макауто, якщо машина існує обновити прбіг власника якщо потрібно
//
//            return null;
//        }
//        System.out.println("сюда дойдем?");
//        workshopService.addLocalRepairType(crmRepairType, workshopName);
//        //TO DO return ви вдало добавили машину html
//        return "redirect:/";
//    }
//}
