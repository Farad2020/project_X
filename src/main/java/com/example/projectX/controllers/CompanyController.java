package com.example.projectX.controllers;

import com.example.projectX.models.*;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Comparator;
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
    public String companyInfo(Model model,
                              @AuthenticationPrincipal ManagementStaff manage) {
        List<UserTeacher> teachers = companyService.getAllCompanyTeachers(manager.getCompanyId());
        //Posts
        model.addAttribute("teachers", teachers );
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


    @GetMapping("company_students/{sort_type}")
    public String companyStudents(Model model,
                                  @PathVariable (name = "sort_type") String sortType,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());
        //Sorting by name currently; Later variations should be added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Comparator<UserStudent> compareByName = Comparator.comparing(UserStudent::getName);

        if(sortType.equals("alp_asc") ){

            Collections.sort(students, compareByName);

        }else if(sortType.equals("alp_desc") ){

            Collections.sort(students, compareByName.reversed());

        }
        model.addAttribute("students", students );
        return "company-students";
    }

    @GetMapping("company_students_filtered")
    public String companyStudentsFiltered(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());

        model.addAttribute("students", students );
        return "company-students-filtered";
    }

    /*
    Заполнить инфой все темплейты
    Настоящие ссылки и страницы для Курсов, Учителей, Студентов, Компаний

    На последок:
    ДжиЭС: Пагинация, Динамичный поиск, И не гейский фильтр!
    */

    @GetMapping("company_teachers")
    public String companyTeachers(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {

        List<UserTeacher> teachers = companyService.getAllCompanyTeachers(manager.getCompanyId());

        model.addAttribute("teachers", teachers );
        return "company-teachers";
    }

    @GetMapping("company_courses")
    public String companyCourses(Model model,
                                 @AuthenticationPrincipal ManagementStaff manager) {

        List<Course> courses = companyService.getAllCompanyCourses(manager.getCompanyId());

        model.addAttribute("courses", courses );
        return "company-courses";
    }


}
