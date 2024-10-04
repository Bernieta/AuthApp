package com.authapp.presentation.controller;

import com.authapp.presentation.dto.AuthCreateUserDTO;
import com.authapp.presentation.dto.AuthUserLoginDTO;
import com.authapp.presentation.dto.AuthUserProfileResponseDTO;
import com.authapp.presentation.dto.AuthUserResponseDTO;
import com.authapp.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/profile-user")
    public ResponseEntity<AuthUserProfileResponseDTO> profileUser(Authentication authentication) {
        return new ResponseEntity<>(
                userDetailsService.profileUser(authentication.getPrincipal().toString()),
                HttpStatus.OK
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthUserResponseDTO> signUp(@RequestBody @Valid AuthCreateUserDTO userDTO) {
        return new ResponseEntity<>(userDetailsService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponseDTO> login(@RequestBody @Valid AuthUserLoginDTO userLoginDTO) {
        return new ResponseEntity<>(userDetailsService.loginUser(userLoginDTO), HttpStatus.OK);
    }
}
