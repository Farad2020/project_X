package com.example.projectX.controllers;

import com.example.projectX.models.Admin;
import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.RegEx;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/")
public class AdminController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public AdminController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("")
    public String adminHome(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "admin-home-page";
    }

    @GetMapping("login")
    public String getAdminLoginView() {
        return "admin-login-page";
    }

    @PostMapping("add_company")
    public String addCompany(@RequestParam(name = "company_name") String companyName,
                             @RequestParam(name = "company_email") String companyEmail,
                             @RequestParam(name = "company_telephone") String companyTelephone) {
        boolean result= companyService.addCompany(new Company(null, companyName, companyEmail, companyTelephone));
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/";
    }

    @GetMapping(path = "company/{company_id}")
    public String getCompanyUsersView(@PathVariable("company_id") UUID id, Model model) {
        Optional<Company> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            List<ManagementStaff> managementStaffList = userAuthenticationService.getAllCompanyManagers(company.get());
            List<UserStudent> students = companyService.getAllCompanyStudents(company.get().getId());

            model.addAttribute("managers", managementStaffList);
            model.addAttribute("students", students);
        }
        model.addAttribute("company_id", id);
        return "admin-company-page";
    }

    @PostMapping("add_manager")
    public String addManager(@RequestParam(name = "login") String managerLogin,
                             @RequestParam(name = "role") int managerRole,
                             @RequestParam(name = "company_id") UUID companyId) {
        System.out.println(companyId);
        boolean result = companyService.addManagerToCompany(managerLogin, managerRole, companyId);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId.toString();
    }

    @GetMapping(path = "company/{company_id}/manager/{manager_id}/edit")
    public String editManager(@PathVariable("company_id") UUID companyId,
                              @PathVariable("manager_id") UUID managerId,
                              Model model) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        Optional<ManagementStaff> manager = companyService.getManagerById(managerId);
        company.ifPresent(value -> model.addAttribute("company", value));
        manager.ifPresent(managementStaff -> model.addAttribute("manager", managementStaff));
        return "admin-company-manager-edit";
    }

    @PostMapping("add_student")
    public String addStudent(@RequestParam(name = "login") String studentLogin,
                             @RequestParam(name = "name") String studentName,
                             @RequestParam(name = "password") String studentPassword,
                             @RequestParam(name = "company_id") UUID companyId) {
        //302f196a-80cd-49b5-b202-df480670eb51
        System.out.println(companyId);
        boolean result = userAuthenticationService.saveUserStudent(studentLogin, studentName, studentPassword, companyId);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId.toString();
    }

//    @PostMapping("edit_manager")
//    public String editManager(@RequestParam(name = "name") String name,
//                              @RequestParam(name = "surname") String surname,
//                              @RequestParam(name = "lastname") String lastname,
//                              @RequestParam(name = "login") String login,
//                              @RequestParam(name = "password") String password,
//                              @RequestParam(name = "email") String email,
//                              @RequestParam(name = "telephone") String telephone,
//                              @RequestParam(name = "")) {
//        return "";
//    }
}
