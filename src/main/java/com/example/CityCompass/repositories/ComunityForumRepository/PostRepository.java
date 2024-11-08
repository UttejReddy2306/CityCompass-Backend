


package com.example.CityCompass.repositories.ComunityForumRepository;

import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;

import com.example.CityCompass.models.ComunityForum.Post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

//    List<Post> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {
            "comments",
            "comments.replies",
            "comments.replies.replies", // Add more levels if needed
            "comments.likes",
            "comments.user",
            "likes",
            "user"
    })
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :postId")
    Post findByIdWithComments(Long postId);



}

