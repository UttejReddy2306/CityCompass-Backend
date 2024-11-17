package com.example.CityCompass.services.ComunityForumServices;

import com.example.CityCompass.dtos.ComunityForumDTOs.LikeResponseDto;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.ComunityForum.PostLike;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    public LikeResponseDto likePost(Long postId, String username) {
        // Retrieve the Post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Retrieve the User by Username
        Users user = userService.getUser(username);

        // Check if the user already liked the post
        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);
        if (existingLike.isPresent()) {
            Boolean isLiked = existingLike.get().getIsLiked();

            if(isLiked == null || !isLiked) {
                existingLike.get().setIsLiked(true);

                postLikeRepository.save(existingLike.get());
                return LikeResponseDto.builder()
                        .postId(postId)
                        .username(username)
                        .message("Post liked successfully!.")
                        .likeCount(postLikeRepository.countByPostIdAndIsLiked(postId, true))
                        .build();
            }
            else{
                return LikeResponseDto.builder()
                        .postId(postId)
                        .username(username)
                        .message("You have already liked this post.")
                        .likeCount(postLikeRepository.countByPostIdAndIsLiked(postId, true))
                        .build();
            }
        }

        // Create and save the new like
        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .isLiked(true)
                .build();
        postLikeRepository.save(like);

        // Return response with updated like count
        long likeCount = postLikeRepository.countByPostIdAndIsLiked(postId,true);
        return LikeResponseDto.builder()
                .postId(postId)
                .username(username)
                .message("Post liked successfully!")
                .likeCount(likeCount)
                .build();
    }

    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostIdAndIsLiked(postId, true);
    }

    public LikeResponseDto unlikePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // Retrieve the User by Username
        Users user = userService.getUser(username);

        // Check if the user already liked the post
        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            Boolean isLiked = existingLike.get().getIsLiked();

            if(isLiked == null || isLiked) {
                existingLike.get().setIsLiked(false);

                postLikeRepository.save(existingLike.get());
            }
            else{
                return LikeResponseDto.builder()
                        .postId(postId)
                        .username(username)
                        .message("You have already unliked this post.")
                        .likeCount(postLikeRepository.countByPostIdAndIsLiked(postId, true))
                        .build();
            }
        }
        long likeCount = postLikeRepository.countByPostIdAndIsLiked(postId,true);
        return LikeResponseDto.builder()
                .postId(postId)
                .username(username)
                .message("Post unliked successfully!")
                .likeCount(likeCount)
                .build();

    }

    public boolean isLikedByUser(Long postId, String username) {
        Users user = userService.getUser(username);
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        Optional<PostLike> postLike = postLikeRepository.findByPostAndUser(post,user);
        if(postLike.isPresent()){
            if(postLike.get().getIsLiked() == null)return false;
            return postLike.get().getIsLiked();
        }
        return false;
    }
}