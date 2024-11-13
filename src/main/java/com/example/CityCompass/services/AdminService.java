package com.example.CityCompass.services;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.ServicesProvided;

import com.example.CityCompass.services.BookServices.ServiceProvidedService;


import com.example.CityCompass.services.FindJobs.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AdminService {


    @Autowired
    ServiceProvidedService serviceProvidedService;

    @Autowired
    CompanyService companyService;
    public List<ServicesProvided> getAllPendingSp() {
        return serviceProvidedService.getAllPendingSp();
    }

    public String updatePermission(Integer spId, String decision) {
        return serviceProvidedService.updatePermission(spId,decision);
    }


    public List<Company> getAllPendingPermission() {
        return companyService.getAllPendingCompanies();
    }

    public String updateCompanyPermission(Integer regId, String decision) {
        return companyService.updateCompanyPermission(regId,decision);
    }


    public List<ServicesProvided> getAllAcceptedSp() {
        return serviceProvidedService.getAllAcceptedSp();
    }

    public List<Company> getAllAcceptedPermission() {
        return companyService.getAllAcceptedCompanies();
    }
}
