package com.example.projectX.controllers;


import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserIdentifier userIdentifier;


    @Autowired
    public AuthenticationController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
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

    @GetMapping("")
    public String home(Model model,
                       @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if( (Boolean) model.getAttribute("isStudent") != null ){
            return "student-home";
        }else if ( (Boolean) model.getAttribute("isManagementStaff") != null ){
            return "company-home";
        }else if ( (Boolean) model.getAttribute("isTeacher") != null ){
            return "teacher-home";
        }
        return "error-page";

    }

}