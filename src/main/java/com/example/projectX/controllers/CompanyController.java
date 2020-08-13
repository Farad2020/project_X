package com.example.projectX.controllers;

import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class CompanyController {
    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public CompanyController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }


    //Компании

    @GetMapping("/company_home")
    public String companyHome(Model model) {
        return "company-home";
    }

    @GetMapping("company_info")
    public String companyInfo(Model model) {
        return "company-info-page";
    }

    @GetMapping("company_management_stuff")
    public String companyManagementStuff(Model model) {
        return "company-management-stuff";
    }

    @GetMapping("company_posts")
    public String companyPosts(Model model) {
        return "company-posts-page";
    }

    @GetMapping("company_students")
    public String companyStudents(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());
        model.addAttribute("students", students );
        return "company-students";
    }

    @GetMapping("company_teachers")
    public String companyTeachers(Model model) {
        return "company-teachers";
    }

    @GetMapping("company_courses")
    public String companyCourses(Model model) {
        return "company-courses";
    }

}
