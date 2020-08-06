package com.example.projectX.controllers;

import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class TestController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public TestController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "home";
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
    //@ResponseStatus(code = HttpStatus.CREATED)
    public String performRegistration(@RequestParam(name = "username") String login,
                                            @RequestParam(name = "name") String name,
                                            @RequestParam(name = "password") String password) {
        boolean result = userAuthenticationService.saveUserStudent(login, name, password, null);
        System.out.println(result);
        return result ? "redirect:/login" : "redirect:/registration";
    }
}