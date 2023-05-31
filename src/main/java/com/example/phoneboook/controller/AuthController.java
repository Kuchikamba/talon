package com.example.phoneboook.controller;

import com.example.phoneboook.payload.request.*;
import com.example.phoneboook.service.RefreshTokenService;
import com.example.phoneboook.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthServiceImpl authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.authentication(loginRequest));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok().body(refreshTokenService.refreshtoken(request));
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signup) throws Exception {
        return ResponseEntity.ok().body(authService.registration(signup));
    }
}
