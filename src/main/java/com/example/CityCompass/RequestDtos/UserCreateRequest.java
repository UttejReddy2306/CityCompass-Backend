package com.example.CityCompass.RequestDtos;

import com.example.CityCompass.models.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    private String username;


    @NotBlank
    private String password;


    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String number;

    public Users toUser(UserCreateRequest userCreateRequest){
        return Users.builder()
                .email(this.email)
                .name(this.name)
                .number(this.number)
                .username(this.username)
                .password(this.password)
                .build();
    }

}
