package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.TimeSlot;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotsDto {

    private Integer timeSlotId;

    private LocalTime localTime;

    private Boolean isAvailable;


}
