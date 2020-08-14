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

    public Optional<ManagementStaff> getManagerById(UUID managerId) {
        return companyDao.getManagerById(managerId);
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

}
