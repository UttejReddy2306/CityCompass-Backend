package com.example.CityCompass.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

import java.util.List;


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

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String experience;

    @Column(nullable = false)
    private String charge;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String license;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    @CreationTimestamp
    private Date createdOn;


    @OneToMany(mappedBy = "servicesProvided",fetch = FetchType.LAZY)
    @JsonIgnoreProperties("servicesProvided")
    @OrderBy("localDate ASC")
    private List<DateSlot> dateSlotList;


    @UpdateTimestamp
    private Date updatedOn;


    private Boolean isSlotsAvailable;



}
