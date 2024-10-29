package com.example.CityCompass.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DateSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("dateSlotList")
    private ServicesProvided servicesProvided;

    private LocalDate localDate;

    @OneToMany(mappedBy = "dateSlot")
    @JsonIgnoreProperties("dateSlot")
    private List<TimeSlot> timeSlotList;

}
