


package com.example.CityCompass.repositories.ComunityForumRepository;

import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;

import com.example.CityCompass.models.ComunityForum.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

//@Query("""
//    SELECT new com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto(
//        p.id, p.title, p.content, p.user.username, p.createdAt,
//        size(p.likes), size(p.comments)
//    )
//    FROM Post p
//    GROUP BY p.id
//    ORDER BY p.createdAt DESC
//""")
//    =--------------------------------------------------------------------
@Query("""
    SELECT new com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto(
        p.id, p.title, p.content, p.user.username, p.createdAt, 
        size(p.likes), size(p.comments), p.imagePath, p.videoPath
    )
    FROM Post p
    LEFT JOIN p.comments c
    GROUP BY p.id, p.title, p.content, p.user.username, p.createdAt, 
             p.imagePath, p.videoPath
    ORDER BY p.createdAt DESC
""")
    List<PostResponseDto> findAllPostsWithCounts();





}

