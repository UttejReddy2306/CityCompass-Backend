package com.example.CityCompass.RequestDtos;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotDto {


    Integer timeSlotId;

    Integer dateSlotId;


    LocalTime localTime;
}
