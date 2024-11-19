package com.example.CityCompass.models.FindJobs;

import com.example.CityCompass.models.Status;
import com.example.CityCompass.models.Users;
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
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String jobDescription;

    @Column(nullable = false)
    private String baseSalary;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String experience;

    @Column(nullable = false)
    private String location;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private Users userId;

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties("jobPostingList")
    private Company company;

    @OneToMany(mappedBy = "jobPosting")
    @JsonIgnoreProperties("jobPosting")
    private List<JobApplication> jobApplicationList;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private Date postedOn;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}