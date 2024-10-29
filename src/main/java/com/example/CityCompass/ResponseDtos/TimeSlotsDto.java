package com.example.CityCompass.ResponseDtos;

import com.example.CityCompass.models.TimeSlot;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotsDto {

    private LocalDate localDate;


}
