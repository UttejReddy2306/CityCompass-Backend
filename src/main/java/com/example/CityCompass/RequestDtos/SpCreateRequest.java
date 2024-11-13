package com.example.CityCompass.RequestDtos;

import com.example.CityCompass.models.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpCreateRequest {

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

    @NotBlank
    private String service;


    private MultipartFile license;

    @NotBlank
    private String experience;

    @NotBlank
    private String charge;


    public Users toUser(SpCreateRequest spCreateRequest){
        return Users.builder()
                .email(this.email)
                .name(this.name)
                .number(this.number)
                .username(this.username)
                .password(this.password)
                .build();
    }

    public ServicesProvided toServiceProvided(SpCreateRequest spCreateRequest){
        return ServicesProvided.builder()
                .service(Services.valueOf(this.service))
                .charge(this.charge)
                .experience(this.experience)
                .status(Status.ACTIVE)
                .permission(Permission.Pending)
                .build();
    }
}
