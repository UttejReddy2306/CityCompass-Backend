package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Services;
import com.example.CityCompass.models.TimeSlot;
import com.example.CityCompass.models.UserRequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicesRequestedDto {

    private Integer serviceRequestedId;

    private String requestedUserName;

    private Services service;

    private String providerUserName;

    private String requestedUserProblem;

    private String charge;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime localTime;

    private Permission permission;

    private String address;

    private UserRequestStatus userRequestStatus;


}
