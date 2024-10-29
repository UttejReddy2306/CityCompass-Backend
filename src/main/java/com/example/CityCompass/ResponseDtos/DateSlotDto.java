package com.example.CityCompass.ResponseDtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DateSlotDto {

    private Integer dateSlotId;

    private LocalDate localDate;

    private List<TimeSlotsDto> timeSlotsDtoList;
}
