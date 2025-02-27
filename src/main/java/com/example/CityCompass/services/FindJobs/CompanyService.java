package com.example.CityCompass.services.FindJobs;


import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.FindJobsDTOs.CompanyUpdateRequestDto;
import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.FindJobs.CompanyRepository;
import com.example.CityCompass.repositories.FindJobs.JobPostRepository;
import com.example.CityCompass.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    S3Service s3Service;

    public void createCompany(Users user, CompanyRegisterRequest companyRegisterRequest) {
        MultipartFile file = companyRegisterRequest.getLicense();
        String license = s3Service.createFile(file);
        Company company = companyRegisterRequest.toCompany(companyRegisterRequest);
        company.setLicense(license);
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

    public String UpdateCompanyDetails(CompanyUpdateRequestDto companyUpdateRequestDto, Users users) {
        Company company = companyRepository.findByUser(users);
        if(companyUpdateRequestDto.getCompanyName() != null) company.setCompanyName(companyUpdateRequestDto.getCompanyName());
        if(companyUpdateRequestDto.getIndustry() != null) company.setIndustry(companyUpdateRequestDto.getIndustry());
        if(companyUpdateRequestDto.getCompanyId() != null) company.setCompanyId(companyUpdateRequestDto.getCompanyId());
        if(companyUpdateRequestDto.getCompanyDetails() != null) company.setCompanyDetails(companyUpdateRequestDto.getCompanyDetails());
        if(companyUpdateRequestDto.getLocation() != null) company.setLocation(companyUpdateRequestDto.getLocation());
        if(companyUpdateRequestDto.getStatus() == Status.INACTIVE) {
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

    public List<Company> getAllAcceptedCompanies() {
        return companyRepository.findByPermission(Permission.Accepted);
    }

    public Integer getAllActiveJobPostingCount(Users users) {
        Company company = companyRepository.findByUser(users);
        return jobPostRepository.findByCompanyAndStatus(company,Status.ACTIVE).size();
    }

    public Integer getAllJobPostingCount() {
        return jobPostRepository.findAllByStatus(Status.ACTIVE).size();
    }
}