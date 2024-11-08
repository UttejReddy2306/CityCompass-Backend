package com.example.CityCompass.services.ComunityForumServices;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.CityCompass.dtos.ComunityForumDTOs.CommentResponseDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostRequestDto;
import com.example.CityCompass.dtos.ComunityForumDTOs.PostResponseDto;
import com.example.CityCompass.models.ComunityForum.Post;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.ComunityForumRepository.CommentRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostLikeRepository;
import com.example.CityCompass.repositories.ComunityForumRepository.PostRepository;
import com.example.CityCompass.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;
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

        MultipartFile imageFile = postRequestDto.getImage();
        MultipartFile videoFile = postRequestDto.getVideo();

        String imagePathName = null;
        String videoPathName = null;

        if (postRequestDto.getImage() != null && !postRequestDto.getImage().isEmpty()) {
            File fileObj = convertMultiPartFileToFile(imageFile);
            imagePathName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//            imagePath = saveFile(postRequestDto.getImage(), "images");
            s3Client.putObject(new PutObjectRequest(bucketName,imagePathName,fileObj));
            fileObj.delete();

        }

        if (postRequestDto.getVideo() != null && !postRequestDto.getVideo().isEmpty()) {
            File fileObj = convertMultiPartFileToFile(videoFile);
            videoPathName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,videoPathName,fileObj));
            fileObj.delete();
        }
        Post post = postRequestDto.toEntity(imagePathName, videoPathName);
        post.setUser(user);
        postRepository.save(post);


        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .preSignedImageUrl(generatePresignedUrl(post.getImagePath(), 30))
                .preSignedVideoUrl(generatePresignedUrl(post.getVideoPath(),30))
                .build();

    }



    public String generatePresignedUrl(String fileName, int expirationInMinutes) {
        if(fileName == null) return null;
        // Set the expiration time for the URL
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (long) expirationInMinutes * 60 * 1000; // Convert minutes to milliseconds
        expiration.setTime(expTimeMillis);

        // Create the request for the pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(com.amazonaws.HttpMethod.GET) // Change to PUT if uploading
                        .withExpiration(expiration);

        // Generate the pre-signed URL
        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }

    private File convertMultiPartFileToFile(MultipartFile imageFile) {
        File convertedFile = new File(Objects.requireNonNull(imageFile.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(imageFile.getBytes());
        }
        catch (IOException e) {
            log.error("Error converting multipart file");
        }
        return convertedFile;
    }

    // Use a fixed upload directory (adjust path if needed)




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


    // Update post by ID
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto) throws IOException {
        // Fetch the post to be updated
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + postId);
        }

        Post post = optionalPost.get();
//        System.out.println("Title: " + postRequestDto.getTitle());
//        System.out.println("Image: " + postRequestDto.getImage());
        // Update text content if provided
        if (postRequestDto.getTitle() != null) {
            post.setTitle(postRequestDto.getTitle());
        }
        if (postRequestDto.getContent() != null) {
            post.setContent(postRequestDto.getContent());


        }

        MultipartFile imageFile = postRequestDto.getImage();
        MultipartFile videoFile = postRequestDto.getVideo();

        String imagePathName = null;
        String videoPathName = null;

        if (postRequestDto.getImage() != null && !postRequestDto.getImage().isEmpty()) {
            File fileObj = convertMultiPartFileToFile(imageFile);
            imagePathName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
//            imagePath = saveFile(postRequestDto.getImage(), "images");
            s3Client.putObject(new PutObjectRequest(bucketName,imagePathName,fileObj));
            fileObj.delete();

        }

        if (postRequestDto.getVideo() != null && !postRequestDto.getVideo().isEmpty()) {
            File fileObj = convertMultiPartFileToFile(videoFile);
            videoPathName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,videoPathName,fileObj));
            fileObj.delete();
        }

       post.setImagePath(imagePathName);
        post.setVideoPath(videoPathName);

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
                    .preSignedImageUrl(generatePresignedUrl(x.getVideoPath(), 30))
                    .postId(x.getId())
                    .commentCount(x.getComments().size())
                    .likeCount(x.getLikes().size())
                    .preSignedVideoUrl(generatePresignedUrl(x.getVideoPath(), 30))
                    .build()
        ).collect(Collectors.toList());
    }
//    public List<PostResponseDto> getLatestPosts() throws IOException {
//        List<Post> posts= postRepository.findAllByOrderByCreatedAtDesc();
//        return posts.stream().map(x -> PostResponseDto.builder()
//                .postId(x.getId())
//                .title(x.getTitle())
//                .content(x.getContent())
//                .username(x.getUser().getUsername())
//                .preSignedImageUrl(generatePresignedUrl(x.getImagePath(),30))
//                .commentCount(x.getComments().size())
//                .preSignedVideoUrl(generatePresignedUrl(x.getVideoPath(),30))
//                .comments(x.getComments().stream().map(y -> CommentResponseDto.builder()
//                        .id(y.getId())
//                        .content(y.getContent())
//                        .username(y.getUser().getUsername())
//                        .createdAt(y.getCreatedAt())
//                        .likeCount(y.getLikes().size())
//                        .replies(y.getReplies())))
//                .likeCount(x.getLikes().size())
//                .build()
//        ).collect(Collectors.toList());
//
//    }

    public List<PostResponseDto> getLatestPosts() {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    private PostResponseDto mapToResponseDto(Post post) {
        int likeCount = post.getLikes() != null ? post.getLikes().size() : 0;
        int commentCount = post.getComments() != null ? post.getComments().size() : 0;
        PostResponseDto dto = PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikes().size())
                .commentCount(post.getComments().size())
                .preSignedImageUrl(generatePresignedUrl(post.getImagePath(),30))
                .preSignedVideoUrl(generatePresignedUrl(post.getVideoPath(),30))
                .build();


        if (post.getComments() != null && !post.getComments().isEmpty()) {
            List<CommentResponseDto> comments = post.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null)
                    .map(commentService::mapToResponseDto)
                    .collect(Collectors.toList());
            dto.setComments(comments);
        }

        return dto;
    }

    public void deleteFile(String fileName){

        s3Client.deleteObject(bucketName,fileName);
    }









}
