package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.SaveJobs;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveJobResponse {

    private Integer jobId;
    private String jobTitle;
    private String companyName;
    private boolean active;

    public static SaveJobResponse fromSaveJobs(SaveJobs saveJobs) {
        return SaveJobResponse.builder()
                .jobId(saveJobs.getJobPosting().getJobId())
                .jobTitle(saveJobs.getJobPosting().getJobTitle())
                .companyName(saveJobs.getJobPosting().getCompany().getCompanyName())
                .active(saveJobs.isActive())
                .build();
    }
}