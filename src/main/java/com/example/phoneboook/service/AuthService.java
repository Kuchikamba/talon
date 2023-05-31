package com.example.phoneboook.service;

import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.payload.request.LoginRequest;
import com.example.phoneboook.payload.request.SignupRequest;
import com.example.phoneboook.payload.response.JwtResponse;

public interface AuthService {
    public UserEntity registration(SignupRequest signup) throws Exception;
    public JwtResponse authentication(LoginRequest loginRequest);
}
