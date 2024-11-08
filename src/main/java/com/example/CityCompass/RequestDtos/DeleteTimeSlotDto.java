package com.example.CityCompass.RequestDtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteTimeSlotDto {
    private Integer ServiceId;

    private List<Integer> localTimeSlotIdList;


}
