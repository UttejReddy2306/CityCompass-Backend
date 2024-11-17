package com.example.CityCompass.Controllers.ComunityForumControllers;

import com.example.CityCompass.dtos.ComunityForumDTOs.CommentRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.services.ComunityForumServices.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @PostMapping("/{postId}")
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestParam(required = false) Long parentCommentId,
            @RequestBody CommentRequestDto commentRequestDto,
            Principal principal) {

        String username = principal.getName();
        CommentResponseDto response = commentService.createComment(postId, parentCommentId, commentRequestDto, username);
        return ResponseEntity.ok(response);
    }

    // Get comments for a post
    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Like a comment
    @PostMapping("/like/{commentId}")
    public ResponseEntity<Void> likeComment(@PathVariable Long commentId, Principal principal) {
        String username = principal.getName();
        commentService.likeComment(commentId, username);
        return ResponseEntity.ok().build();
    }

    // Unlike a comment
    @PostMapping("/unlike/{commentId}")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId, Principal principal) {
        String username = principal.getName();
        commentService.unlikeComment(commentId, username);
        return ResponseEntity.ok().build();
    }








}