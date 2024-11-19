package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.EmploymentType;
import com.example.CityCompass.models.FindJobs.JobApplication;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostingResponseDto {

    private Integer jobId;

    private String jobTitle;

    private String jobDescription;

    private String baseSalary;

    private String experience;

    private String location;

    private Users userId;

    private String profilePicture;

    private Company company;


    private List<JobApplication> jobApplicationList;

    private EmploymentType employmentType;

    private Status status;


    private Date postedOn;

    private Date createdAt;

    private Date updatedAt;

    private boolean isApplied;

    private boolean isSaved;
}
