package com.example.CityCompass.services.ComunityForumServices;


import com.example.CityCompass.dtos.ComunityForumDTOs.CommentRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.Users;

import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserService userService;


    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String username) {
        // Retrieve the Post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Retrieve the User by Username
        Users user = userService.getUser(username);

        // Create the Comment object
        Comment comment = commentRequestDto.to();
        comment.setPost(post);  // Set the Post
        comment.setUser(user);  // Set the User

        // Save the Comment
        commentRepository.save(comment);

        return CommentResponseDto.builder()
                .content(comment.getContent())
                .build();
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
