package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.FindJobs.Company;
import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRegisterRequest {

    @NotBlank
    private String username;


    @NotBlank
    private String password;


    @NotBlank
    private String companyName;

    @NotBlank
    private String companyEmail;

    @NotBlank
    private String contactInfo;

    @NotBlank
    private String companyId;

    @NotBlank
    private String location;

    @NotBlank
    private String industry;

    @NotBlank
    private String companyDetails;


    private Status status;

    public Users companyAsUser(CompanyRegisterRequest companyRegisterRequest){
        return Users.builder()
                .email(this.companyEmail)
                .name(this.companyName)
                .number(this.contactInfo)
                .username(this.username)
                .password(this.password)
                .build();
    }

    public Company toCompany(CompanyRegisterRequest companyRegisterRequest){
        return Company.builder()
                .companyName(this.companyName)
                .companyId(this.companyId)
                .location(this.location)
                .industry(this.industry)
                .companyDetails(this.companyDetails)
                .status(Status.ACTIVE)
                .permission(Permission.Pending)
                .build();
    }
}