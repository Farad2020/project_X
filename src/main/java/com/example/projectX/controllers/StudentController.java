package com.example.projectX.controllers;

import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StudentController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public StudentController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "student-home";
    }


    @GetMapping("/student_profile")
    public String userProfile(Model model,
                              @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);

        /*
        if( userAuthenticationService.getUserStudentByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isStudent", true);
            model.addAttribute("student", (UserStudent)user);
        }
        */
        return "student-account-page";
    }

    @GetMapping("student_attendance")
    public String userAttendance(Model model) {
        return "student-attendance-journal";
    }

    @GetMapping("student_courses")
    public String userCourses(Model model) {
        return "student-courses-page";
    }

    @GetMapping("student_tasks")
    public String userTasks(Model model) {
        return "student-tasks-page";
    }

    @GetMapping("student_teachers")
    public String userTeachers(Model model) {
        return "student-teachers-page";
    }

    @GetMapping("student_home")
    public String home2(Model model) {
        return "student-home";
    }

}
