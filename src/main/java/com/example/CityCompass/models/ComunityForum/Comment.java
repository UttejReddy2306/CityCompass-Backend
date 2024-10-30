//
//
package com.example.CityCompass.models.ComunityForum;

import com.example.CityCompass.models.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Comment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String content;
//
//
//
//    @ManyToOne
//    @JoinColumn(name = "post_id", nullable = false)
//    @JsonIgnore
//    //due to circuler dipendency of user and post we need to tell the spring to ingore the user and post
//    private Post post;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    @JsonIgnore
//    private Users user;
//    private LocalDateTime createdAt;
//
//
//
//    // Automatically set createdAt timestamp on post creation
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//}
// up to here above is working
//---------------------------------------------------------------------------------------------



import com.example.CityCompass.models.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;  // User who created the comment

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
