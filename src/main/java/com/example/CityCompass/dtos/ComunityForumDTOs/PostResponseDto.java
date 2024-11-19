package com.example.CityCompass.dtos.ComunityForumDTOs;

import com.example.CityCompass.models.ComunityForum.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PostResponseDto {
//
//    private Long postId;  // ID of the post
//    private String title;  // Title of the post
//    private String content;  // Content of the post
//    private String username;  // Username of the user who created the post
//    private LocalDateTime createdAt;  // Post creation timestamp
//    private int likeCount;  // Number of likes
//    private int commentCount;  // Number of comments
//    private List<Comment> comments;  // List of comments
//
//    /**
//     * Constructor for mapping post data to DTO, excluding comments initially.
//     */
//    public PostResponseDto(Long postId, String title, String content, String username,
//                           LocalDateTime createdAt, int likeCount, int commentCount) {
//        this.postId = postId;
//        this.title = title;
//        this.content = content;
//        this.username = username;
//        this.createdAt = createdAt;
//        this.likeCount = likeCount;
//        this.commentCount = commentCount;
//    }
//
//    /**
//     * Optional setter to populate comments separately.
//     */
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }
//}
//
//---------------------------------------------------------------------------------------------------------

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String username;
    private String profilePicture;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private String preSignedImageUrl;
    private String preSignedVideoUrl;
    private List<CommentResponseDto> comments;

    private boolean liked;



}

