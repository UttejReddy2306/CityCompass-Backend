package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.ApplicationStatus;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationResponseDto {


    private Integer applicationId;


    private JobPosting jobPosting;

    private Users applicant;


    private ApplicationStatus status;


    private String resume;


    private String coverLetter;


    private Date appliedOn;

    private String profilePicture;
}
