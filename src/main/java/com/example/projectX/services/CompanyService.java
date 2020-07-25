package com.example.projectX.services;

import com.example.projectX.dao.CompanyDao;
import com.example.projectX.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyDao companyDao;

    @Autowired
    public CompanyService(@Qualifier("postgres") CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    public int addCompany(Company company) {
        return companyDao.insertCompany(company);
    }

    public List<Company> getAllCompanies() {
        return companyDao.getAllCompanies();
    }

}
