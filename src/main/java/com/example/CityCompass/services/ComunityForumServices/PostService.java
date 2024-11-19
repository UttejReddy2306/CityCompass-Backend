package com.example.CityCompass.services.ComunityForumServices;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.S3Service;
import com.example.CityCompass.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private S3Service s3Service;

    @Autowired
    private PostLikeService postLikeService;

    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) throws IOException {
        Users user = userService.getUser(username);
        String imagePathName = s3Service.createFile(postRequestDto.getImage());
        String videoPathName = s3Service.createFile(postRequestDto.getVideo());
        Post post = postRequestDto.toEntity(imagePathName, videoPathName);
        post.setUser(user);
        postRepository.save(post);
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .preSignedImageUrl(s3Service.generatePresignedUrl(post.getImagePath(),30))
                .preSignedVideoUrl(s3Service.generatePresignedUrl(post.getVideoPath(), 30))
                .build();
    }

    @Transactional
    public void deletePost(Long postId) {
        // Find the post to be deleted
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + postId);
        }

        Post post = optionalPost.get();

        // Delete associated files (image and/or video) if they exist
        s3Service.deleteFile(post.getImagePath());
        s3Service.deleteFile(post.getVideoPath());

        // Delete likes and comments associated with the posts
        postLikeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);

        // Delete the post
        postRepository.deleteById(postId);
    }
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto) throws IOException {
        // Fetch the post to be updated
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + postId);
        }
        Post post = optionalPost.get();
        // Update text content if provided
        if (postRequestDto.getTitle() != null) {
            post.setTitle(postRequestDto.getTitle());
        }
        if (postRequestDto.getContent() != null) {
            post.setContent(postRequestDto.getContent());
        }
        if(postRequestDto.getImage() != null && !postRequestDto.getImage().isEmpty()){
            post.setImagePath(s3Service.createFile(postRequestDto.getImage()));
        }
        if (postRequestDto.getVideo() != null && !postRequestDto.getVideo().isEmpty()) {
            post.setVideoPath(s3Service.createFile(postRequestDto.getVideo()));
        }
        // Save the updated post
        postRepository.save(post);

        // Return the updated post as DTO
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
    public List<PostResponseDto> getPostsByUser(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream().map(x ->
                PostResponseDto.builder()
                        .preSignedImageUrl(s3Service.generatePresignedUrl(x.getVideoPath(), 30))
                        .postId(x.getId())
                        .commentCount(x.getComments().size())
                        .likeCount(x.getLikes().size())
                        .preSignedVideoUrl(s3Service.generatePresignedUrl(x.getVideoPath(), 30))
                        .build()
        ).collect(Collectors.toList());
    }
    public List<PostResponseDto> getLatestPosts(String username) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(x -> mapToResponseDto(x,username)).toList();
    }

    private PostResponseDto mapToResponseDto(Post post, String username) {
        int likeCount = post.getLikes() != null ? post.getLikes().size() : 0;
        int commentCount = post.getComments() != null ? post.getComments().size() : 0;
        PostResponseDto dto = PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .profilePicture(s3Service.generatePresignedUrl(post.getUser().getProfilePicture(),30))
                .createdAt(post.getCreatedAt())
                .liked(postLikeService.isLikedByUser(post.getId(), username))
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .preSignedImageUrl(s3Service.generatePresignedUrl(post.getImagePath(),30))
                .preSignedVideoUrl(s3Service.generatePresignedUrl(post.getVideoPath(),30))
                .build();


        if (post.getComments() != null && !post.getComments().isEmpty()) {
            List<CommentResponseDto> comments = post.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .map(x -> commentService.mapToResponseDto(x,username))
                    .collect(Collectors.toList());
            dto.setComments(comments);
        }

        return dto;
    }


}