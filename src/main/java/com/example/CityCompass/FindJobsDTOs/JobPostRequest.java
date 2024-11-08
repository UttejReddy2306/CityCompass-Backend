package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.FindJobs.EmploymentType;
import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostRequest {

    private String jobTitle;

    private String jobDescription;

    private String baseSalary;

    private String experience;

    private String location;

    private EmploymentType employmentType;

    private Status status;

    private String companyName;



    public JobPosting toJobPosting(Users userId, Company company) {
        return JobPosting.builder()
                .jobTitle(this.jobTitle)
                .jobDescription(this.jobDescription)
                .baseSalary(this.baseSalary)
                .experience(this.experience)
                .location(this.location)
                .employmentType(this.employmentType)
                .status(this.status)
                .userId(userId)
                .company(company)
                .build();
    }
}