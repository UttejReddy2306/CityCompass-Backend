package com.example.CityCompass.repositories.FindJobs;

import com.example.CityCompass.models.FindJobs.ApplicationStatus;
import com.example.CityCompass.models.FindJobs.JobApplication;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {


    List<JobApplication> findByJobPosting_JobId(Integer jobId);

    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByJobPosting_JobIdAndStatus(Integer jobId, ApplicationStatus status);

    List<JobApplication> findByApplicant_IdAndJobPosting_JobIdAndStatus(Integer applicantId, Integer jobId, ApplicationStatus status);

    List<JobApplication> findByApplicant_Id(Integer applicantId);

    List<JobApplication> findByApplicant(Users users);

    JobApplication findByJobPostingAndApplicant(JobPosting jobPosting, Users applicant);

    boolean existsByApplicantAndJobPosting(Users users, JobPosting jobPosting);
}