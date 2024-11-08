package com.example.CityCompass.RequestDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDto {

    @NotNull
    private Integer serviceId;

    private String userReason;

    @NotBlank
    private String address;

    @NotNull
    private Integer localTimeId;


}
