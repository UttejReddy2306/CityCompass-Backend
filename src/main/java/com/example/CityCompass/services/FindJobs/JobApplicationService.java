package com.example.CityCompass.services.FindJobs;

import com.example.CityCompass.FindJobsDTOs.JobApplicationRequest;

import com.example.CityCompass.models.FindJobs.ApplicationStatus;
import com.example.CityCompass.models.FindJobs.JobApplication;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.UserType;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.FindJobs.JobApplicationRepository;
import com.example.CityCompass.repositories.FindJobs.JobPostRepository;
import com.example.CityCompass.repositories.UserRepository;
import com.example.CityCompass.services.S3Service;
import com.example.CityCompass.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @Autowired
    S3Service s3Service;


    public String createJobApplication(JobApplicationRequest jobApplicationRequest, String username) {
        JobPosting jobPosting = jobPostRepository.findById(jobApplicationRequest.getJobId()).orElse(null);
        Users applicant = userRepository.findByUsername(username);
        if (jobPosting == null || applicant == null) {
            return "Invalid job posting or applicant.";
        }
        if (jobPosting.getStatus() != Status.ACTIVE) {
            return "Cannot apply to an inactive job posting.";
        }
        JobApplication savedJobApplication = this.jobApplicationRepository.findByJobPostingAndApplicant(jobPosting,applicant);
        if(savedJobApplication != null) return "You already applied for this Job";
        JobApplication jobApplication = jobApplicationRequest.toJobApplication(jobPosting, applicant);
        jobApplication.setResume(s3Service.createFile(jobApplicationRequest.getResume()));
        jobApplication.setCoverLetter(s3Service.createFile(jobApplicationRequest.getCoverLetter()));
        jobApplicationRepository.save(jobApplication);
        return "Job application submitted successfully.";
    }

    public List<JobApplication> getApplicationsByApplicant(String username) {
        Users users =  userService.getUser(username);
        return jobApplicationRepository.findByApplicant(users);
    }

    public List<JobApplication> getApplicationsByJobPosting(Integer jobId) {
        return jobApplicationRepository.findByJobPosting_JobId(jobId);
    }

    public List<JobApplication> getApplicationsByStatus(ApplicationStatus status) {
        return jobApplicationRepository.findByStatus(status);
    }

    public List<JobApplication> getApplicationsByApplicantAndJobAndStatus(Integer applicantId, Integer jobId, ApplicationStatus status) {
        return jobApplicationRepository.findByApplicant_IdAndJobPosting_JobIdAndStatus(applicantId, jobId, status);
    }

    public List<JobApplication> getApplicationsByJobPostingAndStatus(Integer applicantId, Integer jobId, ApplicationStatus status) {
        return jobApplicationRepository.findByJobPosting_JobIdAndStatus( jobId, status);
    }

    public String updateApplicationStatus(Integer applicationId, ApplicationStatus newStatus, String username) {
        JobApplication application = jobApplicationRepository.findById(applicationId).orElse(null);
        if (application == null) {
            return "Application not found.";
        }

        Users user = userService.getUser(username);
        if (user == null || user.getUserType() != UserType.JOB_PROVIDER) {
            return "Unauthorized: Only job providers can update application statuses.";
        }
        JobPosting jobPosting = application.getJobPosting();
        if (jobPosting.getUserId() != user) {
            return "Unauthorized: You do not own this job posting.";
        }

        application.setStatus(newStatus);
        jobApplicationRepository.save(application);
        return "Application status updated successfully.";
    }
}