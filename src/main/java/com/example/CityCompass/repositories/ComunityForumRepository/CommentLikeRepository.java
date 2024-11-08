package com.example.CityCompass.repositories.ComunityForumRepository;



import com.example.CityCompass.models.ComunityForum.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    int countByCommentId(Long commentId);
}