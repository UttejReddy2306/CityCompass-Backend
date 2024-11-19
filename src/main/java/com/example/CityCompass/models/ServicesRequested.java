package com.example.CityCompass.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRequestStatus userRequestStatus;


    @JoinColumn
    @ManyToOne
    private TimeSlot timeSlot;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String requestedUserProblem;


    private String address;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;
}
