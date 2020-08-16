package com.example.projectX.dao;

import com.example.projectX.models.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyDao {

    boolean insertCompany(UUID id, Company company);

    default boolean insertCompany(Company company) {
        UUID id = UUID.randomUUID();
        return insertCompany(id, company);
    }

    Optional<Company> getCompanyByName(String name);

    Optional<Company> getCompanyById(UUID id);

    List<Company> getAllCompanies();

    boolean insertManager(String managerLogin, int role, UUID companyId);

    boolean insertTeacher(String teacherName, String teacherLogin, UUID companyId);

    boolean insertCourse(UUID companyId, Course course);

    Optional<ManagementStaff> getManagerById(UUID id);

    Optional<UserTeacher> getTeacherById(UUID teacherId);

    Optional<UserStudent> getStudentById(UUID studentId);

    Optional<Course> getCourseById(UUID courseId);

    List<UserStudent> getAllCompanyStudents(UUID companyId);

    List<UserTeacher> getAllCompanyTeachers(UUID companyId);

    List<Course> getAllCompanyCourses(UUID companyId);

    List<UserStudent> getAllStudentsOfCourse(UUID courseId);

    List<UserStudent> getAllCompanyStudentsThatNotInCourse(UUID companyId, UUID courseId);

    boolean updateCourseById(UUID courseId, Course course);

    boolean addStudentToCourse(UUID studentId, UUID courseId);

    boolean deleteStudentFromCourse(UUID studentId, UUID courseId);

    List<ManagementStaff> selectAllCompanyManagers(Company company);

    List<Course> getAllStudentCourses(UUID studentId);

    List<UserTeacher> getAllStudentTeachers(UUID studentId);

    List<Course> getAllTeacherCourses(UUID teacherId);

    List<UserStudent> getAllTeacherStudents(UUID teacherId);

    List<Schedule> getAllCourseSchedule(UUID courseId);

    boolean addScheduleToCourse(Schedule schedule, UUID courseId);

    boolean deleteScheduleFromCourse(UUID scheduleId);

}
