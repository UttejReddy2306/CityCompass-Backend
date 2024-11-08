package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.JobPosting;
import com.example.CityCompass.models.FindJobs.SaveJobs;
import com.example.CityCompass.models.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveJobRequest {


    @NotBlank
    private Integer jobId;

    public SaveJobs toSaveJobs(Users user, JobPosting jobPosting) {
        return SaveJobs.builder()
                .user(user)
                .jobPosting(jobPosting)
                .active(true)
                .build();
    }
}