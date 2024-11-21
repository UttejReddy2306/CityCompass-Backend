package com.example.CityCompass.RequestDtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String number;

    @NotNull
    private Integer serviceId;

    @NotBlank
    private String userReason;

    @Size(max = 3, message = "The Maximum Value of List is 3")
    private List<MultipartFile> multipartFileList;

    @NotBlank
    private String address;

    @NotNull
    private Integer localTimeId;


}
