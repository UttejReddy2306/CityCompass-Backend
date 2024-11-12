package com.example.CityCompass.services.FindJobs;


import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.FindJobs.CompanyRepository;
import com.example.CityCompass.repositories.FindJobs.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    public void createCompany(Users user, CompanyRegisterRequest companyRegisterRequest) {
        Company company = companyRegisterRequest.toCompany(companyRegisterRequest);
        company.setUser(user);
        companyRepository.save(company);
    }

    public List<Company> getAllPendingCompanies() {
        return companyRepository.findByPermission(Permission.Pending);
    }

    public String updateCompanyPermission(Integer regId, String decision) {
        Company company = companyRepository.findByRegistrationId(regId);
        if (company != null) {
            company.setPermission(decision.equalsIgnoreCase("accepted") ? Permission.Accepted : Permission.Rejected);
            companyRepository.save(company);
            return "Update successful";
        }
        return "Invalid registration ID";
    }

    public List<Company> getActiveApprovedCompanies() {
        return companyRepository.findByStatusAndPermission(Status.ACTIVE, Permission.Accepted);
    }

    public Company getCompanyByName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    public Company getCompanyByUser(Users user) {
        return companyRepository.findByUser(user);
    }

    public Company getCompanyByRegistrationId(int registrationId) {
        return companyRepository.findByRegistrationId(registrationId);
    }

    public List<Company> getAllApprovedCompanies(Users user) {
        return companyRepository.findByUserAndPermissionAndStatus(user, Permission.Accepted, Status.ACTIVE);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findByStatusAndPermission(Status.ACTIVE,Permission.Accepted);
    }

    public String UpdateCompanyDetails(CompanyRegisterRequest companyRegisterRequest, Users users) {
        Company company = companyRepository.findByUser(users);
        if(companyRegisterRequest.getCompanyDetails() != null) company.setCompanyDetails(companyRegisterRequest.getCompanyDetails());
        if(companyRegisterRequest.getLocation() != null) company.setLocation(companyRegisterRequest.getLocation());
        if(companyRegisterRequest.getStatus() == Status.INACTIVE) {
            List<JobPosting> jobPostingList = jobPostRepository.findByCompany(company);
            for(JobPosting jobPosting : jobPostingList){
                jobPosting.setStatus(Status.INACTIVE);
                jobPostRepository.save(jobPosting);
            }

        }
        companyRepository.save(company);
        return "Updated Successfully";
    }

    public Company getCompanyDetails(Users users) {
        return companyRepository.findByUser(users);
    }
}