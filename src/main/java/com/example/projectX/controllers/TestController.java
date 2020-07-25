package com.example.projectX.controllers;

import com.example.projectX.models.Company;
import com.example.projectX.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    private final CompanyService companyService;

    @Autowired
    public TestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "home";
    }
}