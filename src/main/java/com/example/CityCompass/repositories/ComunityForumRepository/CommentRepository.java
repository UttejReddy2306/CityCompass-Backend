package com.example.CityCompass.repositories.ComunityForumRepository;

import com.example.CityCompass.models.ComunityForum.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
//    @Modifying
//    @Transactional
//    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt DESC")
//    List<Comment> findCommentsByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY c.createdAt DESC")
    List<Comment> findRootCommentsByPostId(Long postId);
}

