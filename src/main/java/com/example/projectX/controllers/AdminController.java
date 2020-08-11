package com.example.projectX.controllers;

import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            model.addAttribute("managers", managementStaffList);
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
                              @PathVariable("manager_id") UUID managerId) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        //Optional<ManagementStaff> manager = userAuthenticationService.get
        return "redirect:/";
    }
}
