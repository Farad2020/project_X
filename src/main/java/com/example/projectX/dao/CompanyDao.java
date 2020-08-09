package com.example.projectX.dao;

import com.example.projectX.models.Company;

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

}
