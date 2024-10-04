package com.authapp.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthUserResponseDTO {

    private String email;
    private String message;
    private String accessToken;
    private boolean status;

}
