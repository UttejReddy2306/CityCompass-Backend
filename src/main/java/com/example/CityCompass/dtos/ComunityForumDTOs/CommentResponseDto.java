package com.example.CityCompass.dtos.ComunityForumDTOs;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<CommentResponseDto> replies;
}
