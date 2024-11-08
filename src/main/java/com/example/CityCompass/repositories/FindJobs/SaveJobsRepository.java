package com.example.CityCompass.repositories.FindJobs;

import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.FindJobs.SaveJobs;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaveJobsRepository extends JpaRepository<SaveJobs, Integer> {

    // Find all active saved jobs for a specific user
    List<SaveJobs> findByUserAndActiveTrue(Users user);

    // Check if a specific job is already saved by a user
    SaveJobs findByUserAndJobPosting(Users user, JobPosting jobPosting);

    // Find active saved job by user and job posting
    SaveJobs findByUserAndJobPostingAndActiveTrue(Users user, JobPosting jobPosting);
}