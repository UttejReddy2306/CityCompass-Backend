package com.example.CityCompass.dtos;

import com.example.CityCompass.models.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInRequest {

    @NotBlank
    private String username;


    @NotBlank
    private String password;


    public Users to(UserSignInRequest userSignInRequest){
        return Users.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
