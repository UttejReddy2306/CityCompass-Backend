package com.example.CityCompass.services;

import com.example.CityCompass.dtos.UserCreateRequest;
import com.example.CityCompass.dtos.UserSignInRequest;
import com.example.CityCompass.models.UserType;
import com.example.CityCompass.models.Users;
import com.example.CityCompass.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    public String createUser(UserCreateRequest userCreateRequest) {
        Users users = userCreateRequest.toUser(userCreateRequest);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setUserType(UserType.USER);
        this.userRepository.save(users);
        return "SUCCESSFUL";
    }

    public String verify(UserSignInRequest userSignInRequest, HttpServletResponse httpServletResponse) {
        Users users = userSignInRequest.to(userSignInRequest);
        Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        if(authentication.isAuthenticated()){

            users = this.userRepository.findByUsername(users.getUsername());
            String token = this.jwtService.generateToken(users.getUsername(), users.getUserType());
            httpServletResponse.setHeader("Authorized", "Bearer "+ token);
            return token;
        }
        else return "Unauthenticated";
    }

    public Users getDetails(String username) {
        return userRepository.findByUsername(username);
    }
}
