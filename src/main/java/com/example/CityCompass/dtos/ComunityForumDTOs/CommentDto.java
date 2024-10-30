package com.example.CityCompass.dtos.ComunityForumDTOs;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private String content;
    private String username;  // User who made the comment
    private LocalDateTime commentedAt;
}
