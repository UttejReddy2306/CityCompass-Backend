package com.example.CityCompass.dtos.ComunityForumDTOs;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.models.ComunityForum.Post;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    private String content;

}
