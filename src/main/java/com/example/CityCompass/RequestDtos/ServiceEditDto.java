package com.example.CityCompass.RequestDtos;

import com.example.CityCompass.models.ServicesProvided;
import com.example.CityCompass.models.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEditDto {
    private Integer serviceId;

    private String experience;

    private String charge;

    private Status status;

    public ServicesProvided to(){
        return ServicesProvided.builder()
                .id(this.serviceId)
                .experience(this.experience)
                .charge(this.charge)
                .status(this.status)
                .build();
    }
}
