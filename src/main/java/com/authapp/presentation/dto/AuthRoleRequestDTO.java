package com.authapp.presentation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class AuthRoleRequestDTO {

    @Size(max = 3, message = "The user cannot have more than 3 roles")
    private List<String> roles;

}
