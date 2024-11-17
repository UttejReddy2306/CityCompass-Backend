package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicesProvidedDto {

    private Integer serviceId;

    private String name;

    private String serviceName;

    private String experience;

    private String charge;

    private String preSignedUrlLicense;

    private Status status;

    private List<DateSlotDto> dateSlotList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdOn;
}
