package com.example.projectX.controllers;

import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
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

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "student-home";
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


    @GetMapping("/student_profile")
    public String userProfile(Model model,
                              @AuthenticationPrincipal UserDetails user) {
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

    //Учителя

    @GetMapping("/teacher_home")
    public String teacherHome(Model model) {
        return "teacher-home";
    }

    @GetMapping("teacher_account")
    public String teacherAccount(Model model) {
        return "teacher-account-page";
    }

    @GetMapping("teacher_courses")
    public String teacherCourses(Model model) {
        return "teacher-courses-page";
    }

    @GetMapping("teacher_posts")
    public String teacherPosts(Model model) {
        return "teacher-posts-page";
    }

    @GetMapping("teacher_schedule")
    public String teacherSchedule(Model model) {
        return "teacher-schedule-page";
    }

    @GetMapping("teacher_students")
    public String teacherStudents(Model model) {
        return "teacher-students-page";
    }

    @GetMapping("teacher_tasks")
    public String teacherTasks(Model model) {
        return "teacher-tasks-page";
    }

    @GetMapping("generic_course")
    public String genericCourse(Model model) {
        return "course-page";
    }

    @GetMapping("generic_task")
    public String genericTask(Model model) {
        return "task-page";
    }

}