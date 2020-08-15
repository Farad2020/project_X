package com.example.projectX.controllers;


import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class AuthenticationController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;


    @Autowired
    public AuthenticationController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("login")
    public String getLoginView() {
        return "login-page";
    }

    @GetMapping("registration")
    public String getRegistrationView() {
        return "registration-page";
    }

    @PostMapping("perform_registration")
    public String performRegistration(@RequestParam(name = "username") String login,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "password") String password) {
        boolean result = userAuthenticationService.saveUserStudent(login, name, password, null);
        System.out.println(result);
        return result ? "redirect:/login" : "redirect:/registration";
    }

}