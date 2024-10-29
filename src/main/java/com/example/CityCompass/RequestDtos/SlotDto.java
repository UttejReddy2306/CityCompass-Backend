package com.example.CityCompass.RequestDtos;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotDto {

    private LocalDate localDate;
    private List<LocalTime> localTimeList;
}
