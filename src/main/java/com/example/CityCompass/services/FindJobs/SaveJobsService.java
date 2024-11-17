package com.example.CityCompass.services.FindJobs;


import com.example.CityCompass.FindJobsDTOs.SaveJobRequest;
import com.example.CityCompass.FindJobsDTOs.SaveJobResponse;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.FindJobs.SaveJobs;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.FindJobs.JobPostRepository;
import com.example.CityCompass.repositories.FindJobs.SaveJobsRepository;
import com.example.CityCompass.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SaveJobsService {

    @Autowired
    SaveJobsRepository saveJobsRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    UserRepository userRepository;

    public String saveJob(SaveJobRequest saveJobRequest, String username) {
        Users user = userRepository.findByUsername(username);
        JobPosting jobPosting = jobPostRepository.findById(saveJobRequest.getJobId()).orElse(null);

        if (user == null || jobPosting == null) {
            return "Invalid user or job posting.";
        }

        SaveJobs saveJobs = saveJobsRepository.findByUserAndJobPosting(user, jobPosting);
        if (saveJobs != null) {
            return "Job is already saved.";
        }

        saveJobs = saveJobRequest.toSaveJobs(user, jobPosting);
        saveJobsRepository.save(saveJobs);
        return "Job saved successfully.";
    }

    public List<SaveJobResponse> getSavedJobsByUser(String username) {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            return Collections.emptyList();
        }

        List<SaveJobs> savedJobsList = saveJobsRepository.findByUserAndActiveTrue(user);
        List<SaveJobResponse> responseList = new ArrayList<SaveJobResponse>();

        for (SaveJobs saveJobs : savedJobsList) {
            responseList.add(SaveJobResponse.fromSaveJobs(saveJobs));
        }
        return responseList;
    }

    public String unSaveJob(SaveJobRequest saveJobRequest, String username) {
        Users users = userRepository.findByUsername(username);
        JobPosting jobPosting = jobPostRepository.findById(saveJobRequest.getJobId()).orElse(null);
        if(users == null || jobPosting == null){
            return "Invalid user or job posting.";
        }

        SaveJobs saveJobs = saveJobsRepository.findByUserAndJobPosting(users,jobPosting);
        if(saveJobs == null) return "You Already Unsaved this Job Or You haven't saved this job";
        saveJobsRepository.deleteById(saveJobs.getId());
        return "Job Unsaved Successfully.";
    }
}