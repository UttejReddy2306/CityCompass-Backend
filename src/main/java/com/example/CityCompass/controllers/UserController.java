package com.example.CityCompass.controllers;

import com.example.CityCompass.dtos.SpCreateRequest;
import com.example.CityCompass.dtos.UserCreateRequest;
import com.example.CityCompass.dtos.UserSignInRequest;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String createUser(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return this.userService.createUser(userCreateRequest);
    }

    @PostMapping("/login")
    public String longinUser(@Valid @RequestBody UserSignInRequest userSignInRequest, HttpServletResponse httpServletResponse){
        return this.userService.verify(userSignInRequest , httpServletResponse);

    }

    @PostMapping("/register/serviceProvider")
    public String createServiceProvider(@Valid @RequestBody SpCreateRequest spCreateRequest){
        return this.userService.createSpUser(spCreateRequest);
    }

    @GetMapping("/details")
    public Users getUserDetails(HttpServletRequest httpServletRequest){
        return userService.getDetails(httpServletRequest.getAttribute("username").toString());
    }

    @PostMapping("/admin/createAdmin")
    public String createAdmin(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return this.userService.createAdmin(userCreateRequest);
    }

}
