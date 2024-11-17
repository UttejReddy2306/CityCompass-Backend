//package com.example.CityCompass.services.ComunityForumServices;
//
//
//import com.example.CityCompass.dtos.ComunityForumDTOs.CommentRequestDto;
//import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
//import com.example.CityCompass.models.ComunityForum.Comment;
//import com.example.CityCompass.models.ComunityForum.Post;
//import com.example.CityCompass.models.Users;
//
//import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
//import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
//import com.example.CityCompass.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private UserService userService;
//
//
//    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String username) {
//        // Retrieve the Post by ID
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
//
//        // Retrieve the User by Username
//        Users user = userService.getUser(username);
//
//        // Create the Comment object
//        Comment comment = commentRequestDto.to();
//        comment.setPost(post);  // Set the Post
//        comment.setUser(user);  // Set the User
//
//        // Save the Comment
//        commentRepository.save(comment);
//
//        return CommentResponseDto.builder()
//                .content(comment.getContent())
//                .build();
//    }
//
//    public List<Comment> getCommentsByPostId(Long postId) {
//        return commentRepository.findByPostId(postId);
//    }
//}

package com.example.CityCompass.services.ComunityForumServices;

import com.example.CityCompass.dtos.ComunityForumDTOs.CommentRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.models.ComunityForum.CommentLike;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.ComunityForumRepository.CommentLikeRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    // Create a comment or reply
    public CommentResponseDto createComment(Long postId, Long parentCommentId, CommentRequestDto commentRequestDto, String username) {
        Users user = userService.getUser(username);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        Comment comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found with ID: " + parentCommentId));
            comment.setParentComment(parentComment);
        }

        commentRepository.save(comment);

        return mapToResponseDto(comment,username);
    }

    // Get comments for a post
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findRootCommentsByPostId(postId);
        return comments.stream()
                .map(x -> mapToResponseDto(x,null))
                .collect(Collectors.toList());
    }

    // Helper method to map Comment to CommentResponseDto
    public CommentResponseDto mapToResponseDto(Comment comment, String username) {
        int likeCount = comment.getLikes() != null ? comment.getLikes().size() : 0;

        CommentResponseDto dto = CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .likeCount(likeCount)
                .liked(isLikedByUser(comment.getId(), username))
                .build();

        // Map replies
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            List<CommentResponseDto> replies = comment.getReplies().stream()
                    .map(x -> mapToResponseDto(x, username))
                    .collect(Collectors.toList());
            dto.setReplies(replies);
        }

        return dto;
    }

    // Like a comment
    public void likeComment(Long commentId, String username) {
        Users user = userService.getUser(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        // Check if user already liked the comment
        boolean alreadyLiked = comment.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(user.getId()));

        if (!alreadyLiked) {
            CommentLike like = new CommentLike();
            like.setComment(comment);
            like.setUser(user);
            comment.getLikes().add(like);
            commentLikeRepository.save(like);
        }
    }

    // Unlike a comment
    public void unlikeComment(Long commentId, String username) {
        Users user = userService.getUser(username);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + commentId));

        CommentLike like = comment.getLikes().stream()
                .filter(l -> l.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElse(null);

        if (like != null) {
            comment.getLikes().remove(like);
            commentLikeRepository.delete(like);
        }
    }

    public boolean isLikedByUser(Long commentId, String username) {
        Users user = userService.getUser(username);
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) {
            return false;
        }
        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment.get(),user);
        return commentLike != null;

    }
}