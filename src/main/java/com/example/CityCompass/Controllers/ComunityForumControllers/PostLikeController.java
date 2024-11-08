//package com.example.CityCompass.Controllers.ComunityForumControllers;
//
//
//import com.example.CityCompass.dtos.ComunityForumDTOs.LikeResponseDto;
//import com.example.CityCompass.services.ComunityForumServices.PostLikeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/api/likes")
//public class PostLikeController {
//
//    @Autowired
//    private PostLikeService postLikeService;
//
//    @PostMapping("/like/{postId}")
//    public LikeResponseDto likePost(
//            @PathVariable Long postId,
//            HttpServletRequest request) {
//        String username = request.getAttribute("username").toString();  // Extract username
//        return postLikeService.likePost(postId, username);
//    }
//
//
//
//}

package com.example.CityCompass.Controllers.ComunityForumControllers;


import com.example.CityCompass.dtos.ComunityForumDTOs.LikeResponseDto;
import com.example.CityCompass.services.ComunityForumServices.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/likes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/like/{postId}")
    public LikeResponseDto likePost(
            @PathVariable Long postId,
            HttpServletRequest request) {
        String username = request.getAttribute("username").toString();  // Extract username
        return postLikeService.likePost(postId, username);
    }



}

