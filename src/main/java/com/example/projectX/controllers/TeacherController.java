package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.Course;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
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

    // Check for the same company
    @GetMapping("/teacher_home")
    public String teacherHome(Model model,
                              @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {
            return "teacher-home";
        } else {
            return "error-page";
        }
    }


    @GetMapping("teacher_profile/{teacher_id}")
    public String getStudentTeacherById(Model model,
                                        @PathVariable(name = "teacher_id") UUID teacher_id,
                                        @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        Optional<UserTeacher> teacher = companyService.getTeacherById(teacher_id);
        if (!teacher.isPresent()) {
            return "error-page";
        }

        if (model.getAttribute("isTeacher") != null
                && ((UserTeacher) user).getCompanyId().equals(companyService.getTeacherById(teacher_id).get().getCompanyId())){
            /*We know that courses are now necessary, since page will be loaded no matter what*/
            List<Course> courses = companyService.getAllTeacherCourses(teacher_id);
            /* Checking if the teacher is the same as current user */
            if (((UserTeacher) user).getId().equals(teacher_id)) {
                model.addAttribute("isOwner", true);
                model.addAttribute("teacher", teacher.get());
            } else {
                model.addAttribute("teacher", companyService.getTeacherById(teacher_id).get());
            }
            // courses means all courses related to chosen teacher
            model.addAttribute("courses", courses);
            return "teacher-account-page";
        }else if ((model.getAttribute("isManagementStaff") != null &&
                ((ManagementStaff) user).getCompanyId().equals(companyService.getTeacherById(teacher_id).get().getCompanyId())) ||
                (model.getAttribute("isStudent") != null &&
                        ((UserStudent) user).getCompanyId().equals(companyService.getTeacherById(teacher_id).get().getCompanyId()))) {
            List<Course> courses = companyService.getAllTeacherCourses(teacher_id);
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("courses", courses);
            return "teacher-account-page";
        }
        return"error-page";
    }


    @GetMapping("teacher_courses")
    public String teacherCourses(Model model,
                                 @AuthenticationPrincipal UserDetails user) {

        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            List<Course> courses = companyService.getAllTeacherCourses(((UserTeacher) user).getCompanyId());
            /*Better List Required*/
            model.addAttribute("courses", courses);
            return "teacher-courses-page";
        } else {
            return "error-page";
        }
    }

    @GetMapping("teacher_courses/{course_id}")
    public String getTeacherCourseById(Model model,
                                       @PathVariable(name = "course_id") UUID course_id,
                                       @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {
            Course course = companyService.getCourseById(course_id).get();

            model.addAttribute("course", course);
            model.addAttribute("teacher", user);

            return "company-course_page";
        } else {
            return "error-page";
        }
    }

    @GetMapping("teacher_students")
    public String teacherStudents(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            List<UserStudent> students = companyService.getAllCompanyStudents(((UserTeacher) user).getCompanyId());
            model.addAttribute("students", students);

            return "teacher-students-page";
        } else {
            return "error-page";
        }
    }

    @GetMapping("teacher_students/{student_id}")
    public String getTeacherStudentById(Model model,
                                        @PathVariable(name = "student_id") UUID student_id,
                                        @AuthenticationPrincipal UserDetails user) {

        userIdentifier.getUserClass(user, model);

        UserStudent student = companyService.getStudentById(student_id).get();
        if ((Boolean) model.getAttribute("isTeacher") && student != null && ((UserTeacher) user).getCompanyId().equals(student.getCompanyId())) {

            model.addAttribute("student", student);
            model.addAttribute("teacher", user);
            return "company-course_page";
        } else {
            return "error-page";
        }
    }

    @GetMapping("teacher_schedule")
    public String teacherSchedule(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            return "teacher-schedule-page";
        } else {
            return "error-page";
        }
    }

    @GetMapping("teacher_posts")
    public String teacherPosts(Model model) {
        return "teacher-posts-page";
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
