package com.example.CityCompass.FindJobsDTOs;

import com.example.CityCompass.models.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyUpdateRequestDto {

    @NotBlank
    private String companyName;

    @NotBlank
    private String companyId;

    @NotBlank
    private String location;

    @NotBlank
    private String companyDetails;

    @NotBlank
    private Status status;

    @NotBlank
    private String industry;
}
