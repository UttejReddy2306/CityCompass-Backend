package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.DateSlot;
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

    private List<DateSlotDto> dateSlotList;
}
