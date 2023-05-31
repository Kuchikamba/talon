package com.example.phoneboook.service.impl;


import com.example.phoneboook.entity.RefreshToken;
import com.example.phoneboook.entity.RoleEntity;
import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.entity.enums.ERole;
import com.example.phoneboook.payload.request.LoginRequest;
import com.example.phoneboook.payload.request.SignupRequest;
import com.example.phoneboook.payload.response.JwtResponse;
import com.example.phoneboook.repository.RoleRepository;
import com.example.phoneboook.repository.UserRepository;
import com.example.phoneboook.security.jwt.JwtUtils;
import com.example.phoneboook.service.RefreshTokenService;
import com.example.phoneboook.security.service.UserDetailsImpl;
import com.example.phoneboook.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public UserEntity registration(SignupRequest signup) throws Exception {

        if(userRepository.existsByUsername(signup.getUsername())) {
            throw new Exception("Username is already taken!");
        }

        if(userRepository.existsByEmail(signup.getEmail())) {
            throw new Exception("Email is already taken!");
        }

        UserEntity user = new UserEntity(signup.getUsername(), signup.getEmail(), encoder.encode(signup.getPassword()));

        Set<String> strRoles = signup.getRoles();
        Set<RoleEntity> roles = new HashSet<>();

        if(strRoles.isEmpty()) {
            RoleEntity role = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                if(role.equals("admin")) {
                    RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);

        return userRepository.save(user);

    }

    @Override
    public JwtResponse authentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new JwtResponse(accessToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }
}

