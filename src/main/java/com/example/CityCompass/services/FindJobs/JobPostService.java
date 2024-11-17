package com.example.CityCompass.services.FindJobs;


import com.example.CityCompass.FindJobsDTOs.JobPostRequest;
import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.EmploymentType;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.FindJobs.JobApplicationRepository;
import com.example.CityCompass.repositories.FindJobs.JobPostRepository;
import com.example.CityCompass.repositories.FindJobs.SaveJobsRepository;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobPostService {

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    UserService userService;

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    @Autowired
    SaveJobsRepository saveJobsRepository;


    public void createJobPost(JobPostRequest jobPostRequest, Users user, Company company) {

        if (company.getPermission() != Permission.Accepted || company.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException("Company must have ACCEPTED permission and be ACTIVE to create a job post.");
        }
        JobPosting jobPosting = jobPostRequest.toJobPosting(user, company);
        jobPostRepository.save(jobPosting);
    }

    public List<JobPosting> getJobPostingsByCompany(Company company) {
        return jobPostRepository.findByCompany(company);
    }

    public List<JobPosting> getJobPostingsByEmploymentType(String employmentTypeStr) {
        EmploymentType employmentType = EmploymentType.valueOf(employmentTypeStr.toUpperCase());
        return jobPostRepository.findByEmploymentType(employmentType);
    }

    public String updateJobPost(Integer jobId, JobPostRequest jobPostRequest, Users user) {
        JobPosting jobPosting = jobPostRepository.findById(jobId).orElse(null);

        if (jobPosting == null) {
            return "Job posting not found.";
        }

        if (jobPosting.getUserId() != user) {
            return "You are not authorized to update this job posting.";
        }

        jobPosting.setJobTitle(jobPostRequest.getJobTitle());
        jobPosting.setJobDescription(jobPostRequest.getJobDescription());
        jobPosting.setBaseSalary(jobPostRequest.getBaseSalary());
        jobPosting.setExperience(jobPostRequest.getExperience());
        jobPosting.setLocation(jobPostRequest.getLocation());
        jobPosting.setEmploymentType(jobPostRequest.getEmploymentType());
        jobPosting.setStatus(jobPostRequest.getStatus());

        jobPostRepository.save(jobPosting);
        return "Job posting updated successfully.";
    }

    public JobPosting getJobPostingById(Integer jobId) {
        return jobPostRepository.findById(jobId).orElse(null);
    }

    public List<JobPosting> getJobPostingsByCompanyAndJobTitle(Company company, String jobTitle) {
        return jobPostRepository.findByCompanyAndJobTitle(company, jobTitle);
    }

    public List<JobPosting> getJobPostingsByStatus(Status status) {
        return jobPostRepository.findByStatus(status);
    }

    public List<JobPosting> getJobPostingsByStatusAndJobTitle(Status status, String jobTitle) {
        return jobPostRepository.findByStatusAndJobTitle(status, jobTitle);
    }

    public List<Company> getCompaniesByJobTitle(String jobTitle) {
        return jobPostRepository.findCompaniesByJobTitle(jobTitle);
    }

    public List<JobPosting> getAllJobs() {
        return this.jobPostRepository.findByStatus(Status.ACTIVE);
    }

    public List<JobPosting> getAllJobsByCompany(String username) {
        Users users = userService.getUser(username);
        return jobPostRepository.findByUserId(users);
    }

    public boolean findIfApplied(HttpServletRequest request, Integer jobId) {
            Object object = request.getAttribute("username");
            JobPosting jobPosting = jobPostRepository.findById(jobId).orElse(null);

            if(object != null && jobPosting != null){
                Users users = userService.getUser(object.toString());
                return jobApplicationRepository.existsByApplicantAndJobPosting(users,jobPosting);
            }
            return false;

    }

    public boolean findIfSaved(HttpServletRequest request, Integer jobId) {
        Object object = request.getAttribute("username");
        JobPosting jobPosting = jobPostRepository.findById(jobId).orElse(null);
        if(object != null && jobPosting != null){
            Users users = userService.getUser(object.toString());
            return saveJobsRepository.existsByUserAndJobPosting(users,jobPosting) ;
        }
        return false;

    }
}