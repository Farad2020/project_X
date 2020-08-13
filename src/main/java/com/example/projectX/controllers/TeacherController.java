package com.example.projectX.controllers;

import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TeacherController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public TeacherController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
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
