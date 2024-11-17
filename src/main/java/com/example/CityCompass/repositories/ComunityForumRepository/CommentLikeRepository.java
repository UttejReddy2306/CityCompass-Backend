package com.example.CityCompass.repositories.ComunityForumRepository;



import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.models.ComunityForum.CommentLike;
import com.example.CityCompass.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    int countByCommentId(Long commentId);

    CommentLike findByCommentAndUser(Comment comment , Users user);
}