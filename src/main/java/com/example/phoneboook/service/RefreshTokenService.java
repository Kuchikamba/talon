package com.example.phoneboook.service;

import com.example.phoneboook.entity.RefreshToken;
import com.example.phoneboook.payload.request.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    int deleteByUserId(Long userId);

    ResponseEntity refreshtoken(RefreshTokenRequest refreshTokenRequest);
}
