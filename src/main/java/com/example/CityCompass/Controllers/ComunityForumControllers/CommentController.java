package com.example.CityCompass.Controllers.ComunityForumControllers;

import com.example.CityCompass.dtos.ComunityForumDTOs.CommentRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.services.ComunityForumServices.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

@PostMapping("/addcomment/{postId}")
public CommentResponseDto addComment(
        @PathVariable Long postId,
        @RequestBody CommentRequestDto commentRequestDto,
        HttpServletRequest request) {
    String username = request.getAttribute("username").toString();  // Extract username
    return commentService.createComment(postId, commentRequestDto, username);
}

}

