package com.example.CityCompass.FindJobsController;


import com.example.CityCompass.FindJobsDTOs.JobPostingResponseDto;
import com.example.CityCompass.FindJobsDTOs.SaveJobRequest;
import com.example.CityCompass.FindJobsDTOs.SaveJobResponse;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.services.FindJobs.JobPostService;
import com.example.CityCompass.services.FindJobs.SaveJobsService;
import com.example.CityCompass.services.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/saveJobs")
public class SaveJobsController {

    @Autowired
    SaveJobsService saveJobsService;

    @Autowired
    JobPostService jobPostService;

    @Autowired
    S3Service s3Service;

    @PostMapping("/users/save")
    public ResponseEntity<String> saveJob(@RequestBody SaveJobRequest saveJobRequest, HttpServletRequest request) {
        String result = saveJobsService.saveJob(saveJobRequest,request.getAttribute("username").toString());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users/unSave")
    public ResponseEntity<String> unSaveJob(@RequestBody SaveJobRequest saveJobRequest, HttpServletRequest request) {
        String result = saveJobsService.unSaveJob(saveJobRequest,request.getAttribute("username").toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/getAllSavedJobs")
    public ResponseEntity<List<JobPostingResponseDto>> getSavedJobsByUser(HttpServletRequest request) {
        List<SaveJobResponse> savedJobs = saveJobsService
                .getSavedJobsByUser(request.getAttribute("username").toString());
        if (savedJobs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<JobPosting> jobPostingList = savedJobs.stream().map(x -> jobPostService
                .getJobPostingById(x.getJobId()) ).toList();
        List<JobPostingResponseDto> jobPostingResponseDtoList  = jobPostingList.stream()
                .map(x -> JobPostingResponseDto.builder()
                        .jobId(x.getJobId())
                        .jobTitle(x.getJobTitle())
                        .jobApplicationList(x.getJobApplicationList())
                        .jobDescription(x.getJobDescription())
                        .profilePicture(s3Service.generatePresignedUrl(x.getCompany()
                                .getUser().getProfilePicture(),30))
                        .postedOn(x.getPostedOn())
                        .baseSalary(x.getBaseSalary())
                        .experience(x.getExperience())
                        .employmentType(x.getEmploymentType())
                        .location(x.getLocation())
                        .status(x.getStatus())
                        .isApplied(jobPostService.findIfApplied(request,x.getJobId()))
                        .isSaved(jobPostService.findIfSaved(request, x.getJobId()))
                        .build()).toList();
        return ResponseEntity.ok(jobPostingResponseDtoList);

    }
}