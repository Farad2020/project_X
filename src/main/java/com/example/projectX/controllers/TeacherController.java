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
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

            List<Course> courses = companyService.getAllTeacherCourses(((UserTeacher) user).getId());
            model.addAttribute("courses", courses);
            return "teacher-courses-page";
        }
        return "error-page";
    }

    @GetMapping("teacher_students")
    public String teacherStudents(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            List<UserStudent> students = companyService.getAllTeacherStudents(((UserTeacher) user).getId());
            List<Course> courses = companyService.getAllTeacherCourses(((UserTeacher) user).getId());

            model.addAttribute("courses", courses);
            model.addAttribute("students", students);

            return "teacher-students-page";
        }
        return "error-page";
    }

    @GetMapping("teacher_students/{course_id}")
    public String teacherStudentsByCourse(Model model,
                                          @PathVariable(name = "course_id") UUID course_id,
                                          @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher") && companyService.getCourseById(course_id).isPresent()
        && companyService.getCourseById(course_id).get().getCompanyId().equals(((UserTeacher) user).getCompanyId())) {
            //Checking if course is real, and current user and course from the same company
            List<UserStudent> students = companyService.getAllStudentsOfCourse( course_id );
            List<Course> courses = companyService.getAllTeacherCourses(((UserTeacher) user).getId());

            model.addAttribute("course", companyService.getCourseById(course_id).get());
            model.addAttribute("courses", courses);
            model.addAttribute("students", students);

            return "teacher-students-page";
        }
        return "error-page";
    }

    @GetMapping("teacher_schedule")
    public String teacherFullSchedule(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedTeacherSchedule( ((UserTeacher) user).getId() );
            List<Course> courses = companyService.getAllTeacherCourses( ((UserTeacher) user).getId() );
            model.addAttribute("courses", courses);
            model.addAttribute("schedule_map", scheduleMap);
            return "schedule";
        }else{
            return "error-page";
        }
    }

    @GetMapping("teacher_schedule/{course_id}")
    public String teacherSchedule(Model model,
                                  @PathVariable(name = "course_id") UUID course_id,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if ((Boolean) model.getAttribute("isTeacher")) {

            Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedCourseSchedule( course_id );
            List<Course> courses = companyService.getAllTeacherCourses( ((UserTeacher) user).getId() );
            Course course = companyService.getCourseById(course_id).get();
            model.addAttribute("course", course);
            model.addAttribute("courses", courses);
            model.addAttribute("schedule_map", scheduleMap);
            return "student-schedule-page";
        }else{
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

    @GetMapping("courses/{course_id}/attendance")
    public String courseAttendance(@PathVariable(name = "course_id") UUID courseId,
                                   @AuthenticationPrincipal UserDetails user,
                                   Model model) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isTeacher") != null) {
            List<Attendance> attendances = companyService.getAllCourseAttendances(courseId);
            Optional<Course> course = companyService.getCourseById(courseId);
            model.addAttribute("attendances", attendances);
            course.ifPresent(value -> model.addAttribute("course", value));
            Set<String> dates = new HashSet<>();
            for (Attendance attendance : attendances) {
                dates.add(attendance.getDate());
            }
            model.addAttribute("unique_dates", dates);
            return "course-attendance-page";
        }
        return "error-page";
    }

    @PostMapping("add_attendance_to_course")
    public String addAttendanceToCourse(@RequestParam(name = "date") String date,
                                        @RequestParam(name = "course_id") UUID courseId) {
        boolean result = companyService.addAttendanceToCourse(courseId, date);
        return "redirect:/courses/" + courseId + "/attendance";
    }

    @PostMapping("update_attendance")
    public String updateAttendance(@RequestParam Map<String, String> params) {
        UUID courseId = UUID.fromString(params.get("course_id"));
        String date = params.get("date");
        //List<Attendance> attendances = companyService.getAllCourseAttendancesForSpecificDate(courseId, date);
        boolean result = companyService.updateAttendances(params);
        System.out.println(result);
        return "redirect:/courses/" + courseId + "/attendance";
    }

}
