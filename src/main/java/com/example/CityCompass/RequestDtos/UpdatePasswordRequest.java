package com.example.CityCompass.RequestDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePasswordRequest {
    @NotBlank
     private String oldPassword;

    @NotBlank
     private String newPassword;
}
