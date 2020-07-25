package com.example.projectX.dao;

import com.example.projectX.models.Company;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("testDao")
public class CompanyDataAccessService implements CompanyDao{

    private static List<Company> db = new ArrayList<>() {
        {
            add(new Company(UUID.randomUUID(), "IQ hub", "iqhub07@gmail.com", "87777777777"));
            add(new Company(UUID.randomUUID(), "Destination", "destination07@gmail.com", "87776666666"));
            add(new Company(UUID.randomUUID(), "Bolashak", "bolashak07@gmail.com", "87775555555"));
            add(new Company(UUID.randomUUID(), "NIS", "nis07@gmail.com", "87774444444"));
        }
    };

    @Override
    public int insertCompany(UUID id, Company company) {
        db.add(new Company(id, company.getName(), company.getEmail(), company.getTelephone()));
        return 1;
    }

    @Override
    public List<Company> getAllCompanies() {
        return db;
    }
}
