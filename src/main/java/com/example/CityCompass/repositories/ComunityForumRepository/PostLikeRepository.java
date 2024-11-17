package com.example.CityCompass.repositories.ComunityForumRepository;

import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.ComunityForum.PostLike;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    long countByPostIdAndIsLiked(Long postId,Boolean isLiked);
    void deleteByPostId(Long postId);  // Custom method to delete likes by post ID
    Optional<PostLike> findByPostAndUser(Post post, Users user);
}
