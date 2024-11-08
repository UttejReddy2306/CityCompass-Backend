package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.ApplicationStatus;
import com.example.CityCompass.models.FindJobs.JobApplication;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationRequest {

    @NotNull
    private Integer jobId;

    @NotBlank
    private MultipartFile resume;

    private MultipartFile coverLetter;

    @Builder.Default
    private ApplicationStatus applicationStatus = ApplicationStatus.SUBMITTED;

    public JobApplication toJobApplication(JobPosting jobPosting, Users applicant) {
        return JobApplication.builder()
                .jobPosting(jobPosting)
                .applicant(applicant)
                .status(this.applicationStatus != null ? this.applicationStatus : ApplicationStatus.SUBMITTED)
                .build();
    }
}