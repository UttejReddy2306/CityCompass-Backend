package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.Permission;
import com.example.CityCompass.models.Services;
import com.example.CityCompass.models.TimeSlot;
import com.example.CityCompass.models.UserRequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    private String profilePicture;

    private List<String> imageUrlList;

    private String email;

    private String number;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime localTime;

    private Permission permission;

    private String address;

    private UserRequestStatus userRequestStatus;


}
