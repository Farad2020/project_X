package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.*;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class StudentController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserIdentifier userIdentifier;

    @Autowired
    public StudentController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
    }

    @GetMapping("")
    public String home(Model model,
                       @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isStudent") != null) {
            return "student-home";
        } else if (model.getAttribute("IsTeacher") != null) {
            return "teacher-home";
        } else  {
            return "company-home";
        }
    }

    @GetMapping("/student_profile")
    public String userProfile(Model model,
                              @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        return "student-account-page";
    }

    @GetMapping("student_attendance")
    public String userAttendance(Model model) {
        return "student-attendance-journal";
    }

    @GetMapping("student_courses")
    public String userCourses(Model model,
                              @AuthenticationPrincipal UserStudent student) {
        List<Course> courses = companyService.getAllStudentCourses(student.getId());
        model.addAttribute("courses", courses );
        return "student-courses-page";
    }

    @GetMapping("student_courses/{course_id}")
    public String getStudentCourseById(Model model,
                                       @PathVariable(name = "course_id") UUID course_id,
                                       @AuthenticationPrincipal UserStudent student) {
        Optional<Course> course = companyService.getCourseById(course_id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            Optional<UserTeacher> teacher = companyService.getTeacherById(course.get().getTeacherId());
            teacher.ifPresent(userTeacher -> model.addAttribute("teacher", userTeacher));
        }
        return "company-course-page";
    }


    @GetMapping("student_tasks")
    public String userTasks(Model model) {
        return "student-tasks-page";
    }

    @GetMapping("student_teachers")
    public String userTeachers(Model model,
                               @AuthenticationPrincipal UserStudent student) {

        List<UserTeacher> teachers = companyService.getAllStudentTeachers(student.getId());
        model.addAttribute("teachers", teachers );
        return "student-teachers-page";
    }

    /* Filter By Teacher? By Course? Try it! */
    /* Note to reader. I dont rememvber why I used this method*/
    @GetMapping("student_home")
    public String home2(Model model) {
        return "student-home";
    }

}
