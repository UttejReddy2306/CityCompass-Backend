package com.example.CityCompass.models.FindJobs;

import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String jobDescription;

    @Column(nullable = false)
    private String baseSalary;

    @Column(nullable = false)
    private String experience;

    @Column(nullable = false)
    private String location;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private Users userId;

    @JoinColumn
    @ManyToOne
    private Company company;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private Date postedOn;

}