package com.example.CityCompass.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ServicesProvided {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne
    private Users user;

    @Enumerated(EnumType.STRING)
    private Services service;

    private String experience;

    private String charge;

    private Status status;

    private Permission permission;

}
