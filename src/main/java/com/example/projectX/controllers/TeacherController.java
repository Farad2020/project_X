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
        if (model.getAttribute("isTeacher") != null) {
            return "teacher-home";
        } else {
            return "error-page";
        }
    }


    @GetMapping("teacher_profile/{teacher_id}")
    public String getTeacherById(Model model,
                                        @PathVariable(name = "teacher_id") UUID teacher_id,
                                        @AuthenticationPrincipal UserDetails user) {
        // this page can visit anyone from the same company
        // common options would be look through teacher's courses and basic info
        // if visitor of the page is owner himself then he can modify his profile picture and his basic info
        // if visitor of the page is manager who is the BOSS or has authorities to edit teacher's info then they can do everything what teacher can do
        // also manager who is able to delete then he can delete this teacher
        userIdentifier.getUserClass(user, model);
        UserTeacher userTeacher;
        Optional<UserTeacher> optionalUserTeacher = companyService.getTeacherById(teacher_id);
        if (optionalUserTeacher.isPresent()) {
            userTeacher = optionalUserTeacher.get();
        } else {
            return "error-page";
        }
        if (model.getAttribute("current_user") != null &&
            Objects.requireNonNull(model.getAttribute("current_user_company_id")).equals(userTeacher.getCompanyId())) {
            model.addAttribute("teacher", userTeacher);
            model.addAttribute("courses", companyService.getAllTeacherCourses(teacher_id));
            return "teacher-account-page";
        }
        return "error-page";
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

    @PostMapping("teacher_profile/update_info")
    public String updateTeacherInfo(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "surname") String surname,
                                    @RequestParam(name = "lastname") String lastname,
                                    @RequestParam(name = "login") String login,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "telephone") String telephone) {
        return null;
    }

}
