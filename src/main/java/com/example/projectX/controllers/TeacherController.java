package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.Course;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class TeacherController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserIdentifier userIdentifier;

    @Autowired
    public TeacherController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
    }

    //Учителя

    @GetMapping("/teacher_home")
    public String teacherHome(Model model) {
        return "teacher-home";
    }

    @GetMapping("teacher_account")
    public String teacherAccount(Model model,
                                 @AuthenticationPrincipal UserTeacher teacher) {

        model.addAttribute("teacher", teacher );
        return "teacher-account-page";
    }

    @GetMapping("teacher_courses")
    public String teacherCourses(Model model,
                                 @AuthenticationPrincipal UserTeacher teacher) {

        List<Course> courses = companyService.getAllCompanyCourses(teacher.getCompanyId());

        /*Better List Required*/
        model.addAttribute("courses", courses );

        return "teacher-courses-page";
    }

    @GetMapping("teacher_courses/{course_id}")
    public String getTeacherCourseById(Model model,
                                       @PathVariable(name = "course_id") UUID course_id,
                                       @AuthenticationPrincipal UserTeacher teacher) {

        Course course = companyService.getCourseById(teacher.getCompanyId()).get();

        model.addAttribute("course", course );
        model.addAttribute("teacher", teacher );
        return "company-course_page";
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
    public String teacherStudents(Model model,
                                  @AuthenticationPrincipal UserTeacher teacher) {

        List<UserStudent> students = companyService.getAllCompanyStudents(teacher.getCompanyId());

        model.addAttribute("students", students );

        return "teacher-students-page";
    }

    @GetMapping("teacher_students/{student_id}")
    public String getTeacherStudentById(Model model,
                                       @PathVariable(name = "student_id") UUID student_id,
                                       @AuthenticationPrincipal UserTeacher teacher) {

        UserStudent student = companyService.getStudentById(student_id).get();

        model.addAttribute("student", student );
        model.addAttribute("teacher", teacher );
        return "company-course_page";
    }

    @GetMapping("teacher_tasks")
    public String teacherTasks(Model model) {
        return "teacher-tasks-page";
    }

    @GetMapping("task")
    public String genericTask(Model model) {
        return "task-page";
    }

}
