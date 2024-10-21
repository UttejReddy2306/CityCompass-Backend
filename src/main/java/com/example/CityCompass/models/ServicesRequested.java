package com.example.CityCompass.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ServicesRequested {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn
    @ManyToOne
    private Users requestedUser;

    @JoinColumn
    @ManyToOne
    private Users providedUser;

    @JoinColumn
    @ManyToOne
    private ServicesProvided servicesProvided;

    private Status status;



}
