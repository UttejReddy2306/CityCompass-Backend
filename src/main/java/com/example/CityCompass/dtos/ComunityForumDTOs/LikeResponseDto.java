package com.example.CityCompass.dtos.ComunityForumDTOs;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDto {
    private Long postId;
    private String username;  // User who liked the post
    private String message;   // Response message
    private long likeCount;   // Total likes on the post
}
