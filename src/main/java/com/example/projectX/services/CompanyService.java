package com.example.projectX.services;

import com.example.projectX.dao.CompanyDao;
import com.example.projectX.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    private final CompanyDao companyDao;

    @Autowired
    public CompanyService(@Qualifier("postgres") CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public boolean addCompany(Company company) {
        return companyDao.insertCompany(company);
    }

    public Optional<Company> getCompanyById(UUID id) {
        return companyDao.getCompanyById(id);
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAllCompanies();
    }

    public boolean addManagerToCompany(String managerLogin, int managerRole, UUID companyId) {
        return companyDao.insertManager(managerLogin, managerRole, companyId);
    }

    public boolean addTeacherToCompany(String teacherName, String teacherLogin, UUID companyId) {
        return companyDao.insertTeacher(teacherName, teacherLogin, companyId);
    }

    public boolean addCourseToCompany(UUID companyId, Course course) {
        return companyDao.insertCourse(companyId, course);
    }

    public Optional<ManagementStaff> getManagerById(UUID managerId) {
        return companyDao.getManagerById(managerId);
    }

    public Optional<UserTeacher> getTeacherById(UUID teacherId) {
        return companyDao.getTeacherById(teacherId);
    }

    public Optional<UserStudent> getStudentById(UUID studentId) {
        return companyDao.getStudentById(studentId);
    }

    public Optional<Course> getCourseById(UUID courseId) {
        return companyDao.getCourseById(courseId);
    }

    public List<UserStudent> getAllCompanyStudents(UUID companyId) {
        return companyDao.getAllCompanyStudents(companyId);
    }

    public List<UserTeacher> getAllCompanyTeachers(UUID companyId) {
        return companyDao.getAllCompanyTeachers(companyId);
    }

    public List<Course> getAllCompanyCourses(UUID companyId) {
        return companyDao.getAllCompanyCourses(companyId);
    }

    public List<UserStudent> getAllStudentsOfCourse(UUID courseId) {
        return companyDao.getAllStudentsOfCourse(courseId);
    }

    public List<UserStudent> getAllCompanyStudentsThatNotInCourse(UUID companyId, UUID courseId) {
        return companyDao.getAllCompanyStudentsThatNotInCourse(companyId, courseId);
    }

    public boolean updateCourseById(UUID courseId, Course course) {
        return companyDao.updateCourseById(courseId, course);
    }

    public boolean addStudentToCourse(UUID studentId, UUID courseId) {
        return companyDao.addStudentToCourse(studentId, courseId);
    }

    public boolean deleteStudentFromCourse(UUID studentId, UUID courseId) {
        return companyDao.deleteStudentFromCourse(studentId, courseId);

    }

    public List<ManagementStaff> selectAllCompanyManagers(Company company){
        return companyDao.selectAllCompanyManagers(company);
    }

    public List<Schedule> getAllCourseSchedule(UUID courseId) {
        return companyDao.getAllCourseSchedule(courseId);
    }

    public boolean addScheduleToCourse(Schedule schedule, UUID courseId) {
        return companyDao.addScheduleToCourse(schedule, courseId);
    }

    public boolean deleteScheduleFromCourse(UUID scheduleId) {
        return companyDao.deleteScheduleFromCourse(scheduleId);
    }

}
