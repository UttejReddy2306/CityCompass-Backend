package com.example.CityCompass.FindJobsController;

import com.example.CityCompass.FindJobsDTOs.JobApplicationRequest;
import com.example.CityCompass.models.FindJobs.ApplicationStatus;
import com.example.CityCompass.models.FindJobs.JobApplication;
import com.example.CityCompass.services.FindJobs.JobApplicationService;
import com.example.CityCompass.services.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/jobApplications")
public class JobApplicationController {

    @Autowired
    JobApplicationService jobApplicationService;

    @Autowired
    S3Service s3Service;

    @PostMapping(value = "/users/apply",consumes = "multipart/form-data")
    public ResponseEntity<String> createJobApplication(@ModelAttribute JobApplicationRequest jobApplicationRequest, HttpServletRequest request) {
        String result = jobApplicationService.createJobApplication(jobApplicationRequest,request.getAttribute("username").toString());
        if (result == null) {
            return ResponseEntity.badRequest().body("Job application not created. Ensure job is active and applicant exists.");
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/applicant")
    public ResponseEntity<List<JobApplication>> getApplicationsByApplicant(HttpServletRequest request) {
        List<JobApplication> applications = jobApplicationService.getApplicationsByApplicant(request.getAttribute("username").toString());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/company/applications/{jobId}")
    public ResponseEntity<List<JobApplication>> getApplicationsByJobPosting(@PathVariable Integer jobId) {
        List<JobApplication> applications = jobApplicationService.getApplicationsByJobPosting(jobId);
        return ResponseEntity.ok(convert(applications));
    }

    @GetMapping("/company/status/{status}")
    public ResponseEntity<List<JobApplication>> getApplicationsByStatus(@PathVariable ApplicationStatus status) {
        List<JobApplication> applications = jobApplicationService.getApplicationsByStatus(status);
        return ResponseEntity.ok(convert(applications));
    }

    @PutMapping("/company/updateStatus/{applicationId}/{newStatus}")
    public ResponseEntity<String> updateApplicationStatus(
            @PathVariable Integer applicationId,
            @PathVariable ApplicationStatus newStatus,
            HttpServletRequest request) {

        String result = jobApplicationService.updateApplicationStatus(applicationId,  newStatus,request.getAttribute("username").toString());

        if (result.startsWith("Unauthorized")) {
            return ResponseEntity.status(403).body(result);
        } else if (result.equals("Application not found.")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    public List<JobApplication> convert(List<JobApplication> jobApplicationList){
        jobApplicationList.forEach(x -> {
            x.setResume(s3Service.generatePresignedUrl(x.getResume(),30));
            x.setCoverLetter(s3Service.generatePresignedUrl(x.getCoverLetter(),30));
        });
        return jobApplicationList;
    }


}