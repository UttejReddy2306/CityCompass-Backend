package com.example.CityCompass.Controllers.ComunityForumControllers;

import com.example.CityCompass.dtos.ComunityForumDTOs.PostRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;

import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.services.ComunityForumServices.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;


//    @PostMapping("/postNow")
//    public PostResponseDto createPost(
//            @RequestBody PostRequestDto postRequestDto,
//            HttpServletRequest request) {
//        String username = request.getAttribute("username").toString();  // Extract username from request
//        return postService.createPost(postRequestDto, username);
//    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public PostResponseDto createPost(
            @ModelAttribute PostRequestDto postRequestDto,
            @RequestAttribute("username") String username) throws IOException {

        return postService.createPost(postRequestDto, username);
    }



    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUser(@PathVariable Long userId) {
        return postService.getPostsByUser(userId);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/latest")
    public List<PostResponseDto> getLatestPosts() {
    return postService.getLatestPosts();
}









    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "Post deleted successfully!";
    }

    // Update post by ID
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto) throws IOException {
        System.out.println("Received Title: " + postRequestDto.getTitle());
        System.out.println("Received Image: " + postRequestDto.getImage());

        PostResponseDto updatedPost = postService.updatePost(postId, postRequestDto);
        return ResponseEntity.ok(updatedPost);
    }



}






