package com.example.CityCompass.UserController;


import com.example.CityCompass.FindJobsDTOs.CompanyRegisterRequest;
import com.example.CityCompass.RequestDtos.SpCreateRequest;
import com.example.CityCompass.RequestDtos.UserCreateRequest;
import com.example.CityCompass.RequestDtos.UserSignInRequest;

import com.example.CityCompass.models.Users;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

    @PostMapping("/public/register/serviceProvider")
    public String createServiceProvider(@Valid @RequestBody SpCreateRequest spCreateRequest){
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

    @PostMapping("/public/register/jobProvider")
    public String createJobProvider(@Valid @RequestBody CompanyRegisterRequest companyRegisterRequest){
        return this.userService.createJPUser(companyRegisterRequest);
    }


}
