//package com.example.CityCompass.services.ComunityForumServices;
//
//import com.example.CityCompass.dtos.ComunityForumDTOs.LikeResponseDto;
//import com.example.CityCompass.models.ComunityForum.Post;
//import com.example.CityCompass.models.ComunityForum.PostLike;
//import com.example.CityCompass.models.Users;
//import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
//import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
//import com.example.CityCompass.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PostLikeService {
//
//    @Autowired
//    private PostLikeRepository postLikeRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    public LikeResponseDto likePost(Long postId, String username) {
//        // Retrieve the Post by ID
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
//
//        // Retrieve the User by Username
//        Users user = userService.getUser(username);
//
//        // Create and save the PostLike object
//        PostLike like = PostLike.builder()
//                .post(post)
//                .user(user)
//                .build();
//
//        postLikeRepository.save(like);
//
//        // Get the updated like count
//        long likeCount = postLikeRepository.countByPostId(postId);
//
//        // Return the response
//        return LikeResponseDto.builder()
//                .postId(postId)
//                .username(username)
//                .message("Post liked successfully!")
//                .likeCount(likeCount)
//                .build();
//    }
//
//
//    public long getLikeCount(Long postId) {
//        return postLikeRepository.countByPostId(postId);
//    }
//}


//package com.example.CityCompass.services.ComunityForumServices;
//
//import com.example.CityCompass.dtos.ComunityForumDTOs.LikeResponseDto;
//import com.example.CityCompass.models.ComunityForum.Post;
//import com.example.CityCompass.models.ComunityForum.PostLike;
//import com.example.CityCompass.models.Users;
//import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
//import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
//import com.example.CityCompass.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class PostLikeService {
//
//    @Autowired
//    private PostLikeRepository postLikeRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    public LikeResponseDto likePost(Long postId, String username) {
//        // Retrieve the Post by ID
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
//
//        // Retrieve the User by Username
//        Users user = userService.getUser(username);
//
//        // Check if the user already liked the post
//        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);
//        if (existingLike.isPresent()) {
//            return LikeResponseDto.builder()
//                    .postId(postId)
//                    .username(username)
//                    .message("You have already liked this post.")
//                    .likeCount(postLikeRepository.countByPostId(postId))
//                    .build();
//        }
//
//        // Create and save the new like
//        PostLike like = PostLike.builder()
//                .post(post)
//                .user(user)
//                .build();
//        postLikeRepository.save(like);
//
//        // Return response with updated like count
//        long likeCount = postLikeRepository.countByPostId(postId);
//        return LikeResponseDto.builder()
//                .postId(postId)
//                .username(username)
//                .message("Post liked successfully!")
//                .likeCount(likeCount)
//                .build();
//    }
//
//    public long getLikeCount(Long postId) {
//        return postLikeRepository.countByPostId(postId);
//    }
//}

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
            return LikeResponseDto.builder()
                    .postId(postId)
                    .username(username)
                    .message("You have already liked this post.")
                    .likeCount(postLikeRepository.countByPostId(postId))
                    .build();
        }

        // Create and save the new like
        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();
        postLikeRepository.save(like);

        // Return response with updated like count
        long likeCount = postLikeRepository.countByPostId(postId);
        return LikeResponseDto.builder()
                .postId(postId)
                .username(username)
                .message("Post liked successfully!")
                .likeCount(likeCount)
                .build();
    }

    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
}
