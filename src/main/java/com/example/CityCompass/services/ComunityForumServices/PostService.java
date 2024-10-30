package com.example.CityCompass.services.ComunityForumServices;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;
import com.example.CityCompass.models.ComunityForum.Comment;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
@Autowired
private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostLikeRepository postLikeRepository;
//    private static final String UPLOAD_DIR = "C:/uploads";
private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";


    //    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) {
//        Users user = userService.getUser(username);
//
//        Post post = postRequestDto.to();
//        post.setUser(user);
//
//        postRepository.save(post);
//
//        return PostResponseDto.builder()
//                .postId(post.getId())
//                .title(post.getTitle())
//                .content(post.getContent())
////                .likeCount(post.getLikes().size())
////                .commentCount(post.getComments().size())
//                .build();
//    }
private static final String IMAGE_DIR = "uploads/images/";
    private static final String VIDEO_DIR = "uploads/videos/";

    public PostResponseDto createPost(PostRequestDto postRequestDto, String username) throws IOException {
        Users user = userService.getUser(username);

        String imagePath = null;
        String videoPath = null;

        if (postRequestDto.getImage() != null && !postRequestDto.getImage().isEmpty()) {
            imagePath = saveFile(postRequestDto.getImage(), "images");
        }

        if (postRequestDto.getVideo() != null && !postRequestDto.getVideo().isEmpty()) {
            videoPath = saveFile(postRequestDto.getVideo(), "videos");
        }



        Post post = postRequestDto.toEntity(imagePath, videoPath);
        post.setUser(user);
        postRepository.save(post);


        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imagePath(post.getImagePath())
                .videoPath(post.getVideoPath())
                .build();
    }

    // Use a fixed upload directory (adjust path if needed)


    public String saveFile(MultipartFile file, String subDir) throws IOException {
        // Create the full path: C:/uploads/images/ or C:/uploads/videos/
        Path uploadDirPath = Paths.get(UPLOAD_DIR, subDir);

        // Create directories if they don't exist
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        // Generate a unique filename to avoid conflicts
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadDirPath.resolve(filename);

        // Save the file to the path
        file.transferTo(filePath.toFile());

        // Return the path as a string to store in the database
        return filePath.toString();
    }

    // Delete post by ID
//    @Transactional
//    public void deletePost(Long postId) {
//        if (postRepository.existsById(postId)) {
//            // Delete all likes associated with the post
//            postLikeRepository.deleteByPostId(postId);
//
//            // Delete all comments associated with the post
//            commentRepository.deleteByPostId(postId);
//
//            // Delete the post
//            postRepository.deleteById(postId);
//        } else {
//            throw new RuntimeException("Post not found with ID: " + postId);
//        }
//    }



    @Transactional
    public void deletePost(Long postId) {
        // Find the post to be deleted
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + postId);
        }

        Post post = optionalPost.get();

        // Delete associated files (image and/or video) if they exist
        deleteFile(post.getImagePath());
        deleteFile(post.getVideoPath());

        // Delete likes and comments associated with the post
        postLikeRepository.deleteByPostId(postId);
        commentRepository.deleteByPostId(postId);

        // Delete the post
        postRepository.deleteById(postId);
    }

    // Utility method to delete a file from the local file system
    private void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                Path path = Paths.get(filePath);
                Files.deleteIfExists(path);  // Delete the file if it exists
                System.out.println("Deleted file: " + filePath);
            } catch (IOException e) {
                System.err.println("Failed to delete file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    // Update post by ID
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto) throws IOException {
        // Fetch the post to be updated
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + postId);
        }

        Post post = optionalPost.get();
        System.out.println("Title: " + postRequestDto.getTitle());
        System.out.println("Image: " + postRequestDto.getImage());
        // Update text content if provided
        if (postRequestDto.getTitle() != null) {
            post.setTitle(postRequestDto.getTitle());
        }
        if (postRequestDto.getContent() != null) {
            post.setContent(postRequestDto.getContent());
        }

        // Handle new image upload if provided
        if (postRequestDto.getImage() != null && !postRequestDto.getImage().isEmpty()) {
            String imagePath = saveFile(postRequestDto.getImage(), "images");
            post.setImagePath(imagePath);
        }

        // Handle new video upload if provided
        if (postRequestDto.getVideo() != null && !postRequestDto.getVideo().isEmpty()) {
            String videoPath = saveFile(postRequestDto.getVideo(), "videos");
            post.setVideoPath(videoPath);
        }

        // Save the updated post
        postRepository.save(post);

        // Return the updated post as DTO
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imagePath(post.getImagePath())
                .videoPath(post.getVideoPath())
                .build();
    }






    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }
    public List<PostResponseDto> getLatestPosts() {
        List<PostResponseDto> posts= postRepository.findAllPostsWithCounts();
        for (PostResponseDto post : posts) {
            List<Comment> comments = commentRepository.findCommentsByPostId(post.getPostId());
            post.setComments(comments);  // Add comments to the DTO
        }
        return posts;
    }







}
