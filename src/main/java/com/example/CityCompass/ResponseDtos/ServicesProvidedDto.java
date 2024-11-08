package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.DateSlot;
import com.example.CityCompass.models.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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


    private Status status;

    private List<DateSlotDto> dateSlotList;
}
