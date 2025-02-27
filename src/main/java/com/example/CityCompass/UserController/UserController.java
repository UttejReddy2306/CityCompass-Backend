package com.example.CityCompass.UserController;


import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.RequestDtos.SpCreateRequest;
import com.example.CityCompass.RequestDtos.UpdatePasswordRequest;
import com.example.CityCompass.RequestDtos.UserCreateRequest;
import com.example.CityCompass.RequestDtos.UserSignInRequest;

import com.example.CityCompass.models.Users;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/public/register")
    public String createUser(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return this.userService.createUser(userCreateRequest);
    }

    @PostMapping("/public/login")
    public String longinUser(@Valid @RequestBody UserSignInRequest userSignInRequest, HttpServletResponse httpServletResponse){
        return this.userService.verify(userSignInRequest , httpServletResponse);

    }

    @PostMapping(value = "/public/register/serviceProvider" , consumes = "multipart/form-data")
    public String createServiceProvider(@Valid @ModelAttribute SpCreateRequest spCreateRequest){
        return this.userService.createSpUser(spCreateRequest);
    }

    @GetMapping("/all/details")
    public Users getUserDetails(HttpServletRequest httpServletRequest){
        return userService.getDetails(httpServletRequest.getAttribute("username").toString());
    }

    @PostMapping("/admin/createAdmin")
    public String createAdmin(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return this.userService.createAdmin(userCreateRequest);
    }

    @PostMapping(value = "/public/register/jobProvider" , consumes = "multipart/form-data")
    public String createJobProvider(@Valid @ModelAttribute CompanyRegisterRequest companyRegisterRequest){
        return this.userService.createJPUser(companyRegisterRequest);
    }


    @PostMapping(value = "/all/updateProfilePicture",  consumes = "multipart/form-data")
    public ResponseEntity<String> createProfilePicture(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String fileType = file.getContentType();
        if (fileType == null || (!fileType.equals("image/jpeg") && !fileType.equals("image/png"))) {
            return ResponseEntity.badRequest().body("Invalid file format. Only JPEG and PNG are allowed.");
        }
        long maxFileSize = 2 * 1024 * 1024;
        if (file.getSize() > maxFileSize) {
            return ResponseEntity.badRequest().body("File is too large. Maximum size allowed is 2 MB.");
        }
         return ResponseEntity.ok(userService.createProfilePicture(file
                 ,request.getAttribute("username").toString()));
    }

    @PatchMapping("/all/updateProfile")
    public String updateProfile(@RequestParam(value = "email",required = false) String email
            , @RequestParam(value = "number",required = false) String number
            ,@RequestParam(value = "name",required = false) String name, HttpServletRequest request){
        return userService.updateProfile(email,number,name,request.getAttribute("username").toString());
    }

    @PatchMapping("/all/updatePassword")
    public String updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request){
        return userService.updatePassword(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword()
                ,request.getAttribute("username").toString());
    }


    @PostMapping("/public/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {

        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PostMapping("/public/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam(value = "token") String token, @RequestParam("newPassword") String newPassword) {
        boolean resetSuccessful = userService.validateTokenAndResetPassword(token, newPassword);
        if (resetSuccessful) {
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }





}
