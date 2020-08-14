package com.example.projectX.dao;

import com.example.projectX.models.*;
import org.checkerframework.checker.nullness.Opt;

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

    Optional<ManagementStaff> getManagerById(UUID id);

    List<UserStudent> getAllCompanyStudents(UUID companyId);

    List<UserTeacher> getAllCompanyTeachers(UUID companyId);

    List<Course> getAllCompanyCourses(UUID companyId);
}
