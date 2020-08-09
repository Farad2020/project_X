package com.example.projectX.controllers;

import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String performRegistration(@RequestParam(name = "username") String login,
                                      @RequestParam(name = "name") String name,
                                      @RequestParam(name = "password") String password) {
        boolean result = userAuthenticationService.saveUserStudent(login, name, password, null);
        System.out.println(result);
        return result ? "redirect:/login" : "redirect:/registration";
    }


    @GetMapping("/profile")
    public String profile(Model model) {
        return "account-page";
    }

    @GetMapping("/user_attendance")
    public String userAttendance(Model model) {
        return "my-attendance-journal";
    }

    @GetMapping("user_courses")
    public String user_courses(Model model) {
        return "my-courses-page";
    }

    @GetMapping("user_tasks")
    public String user_tasks(Model model) {
        return "my-tasks-page";
    }

    @GetMapping("user_teachers")
    public String user_teachers(Model model) {
        return "my-teachers-page";
    }

    @GetMapping("home")
    public String home2(Model model) {
        return "home";
    }
}