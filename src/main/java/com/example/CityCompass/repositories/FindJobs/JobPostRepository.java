package com.example.CityCompass.repositories.FindJobs;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.EmploymentType;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPosting, Integer> {

    // Find job postings by status (e.g., ACTIVE)
    List<JobPosting> findByStatus(Status status);

    List<JobPosting> findByStatusAndJobTitleAndCompany(Status status, String jobTitle, Company company);

    // Find job postings by the company that posted them
    List<JobPosting> findByCompany(Company company);

    // Find job postings by the company that posted them
    List<JobPosting> findByCompanyAndJobTitle(Company company ,  String JobTitle);

    // Find job postings by employment type (Full-time, Part-time, Contract)
    List<JobPosting> findByEmploymentType(EmploymentType employmentType);

    List<JobPosting> findByStatusAndJobTitle(Status status , String JobTitle);

    @Query("SELECT DISTINCT jp.company FROM JobPosting jp WHERE jp.jobTitle = :jobTitle")
    List<Company> findCompaniesByJobTitle(@Param("jobTitle") String jobTitle);

    List<Company> findAllByStatus(Status status);

    List<JobPosting> findByUserId(Users users);
}