package com.example.CityCompass.dtos.ComunityForumDTOs;
//
//import com.example.CityCompass.models.ComunityForum.Post;
//import lombok.*;
//
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class PostRequestDto {
//
//    String title;
//
//    String content;
//
//
//    public Post to(){
//        return Post.builder()
//                .content(this.content)
//                .title(this.title)
//
//                .build();
//    }
//}
//----------------------------------------------------------------------------------------------------
import com.example.CityCompass.models.ComunityForum.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto {

    private String title;
    private String content;
    private MultipartFile image;  // Optional image file
    private MultipartFile video;  // Optional video file

    public Post toEntity(String imagePath, String videoPath) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .imagePath(imagePath)
                .videoPath(videoPath)
                .build();
    }
}
