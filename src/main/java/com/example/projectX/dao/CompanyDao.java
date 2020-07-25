package com.example.projectX.dao;

import com.example.projectX.models.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyDao {

    int insertCompany(UUID id, Company company);

    default int insertCompany(Company company) {
        UUID id = UUID.randomUUID();
        return insertCompany(id, company);
    }

    List<Company> getAllCompanies();

}
