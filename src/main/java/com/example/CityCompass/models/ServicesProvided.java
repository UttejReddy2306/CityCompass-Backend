package com.example.CityCompass.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Services service;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String charge;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    @CreationTimestamp
    private Date createdOn;

    @Column
    private TimeSlots timeSlots;

    @UpdateTimestamp
    private Date updatedOn;


}
