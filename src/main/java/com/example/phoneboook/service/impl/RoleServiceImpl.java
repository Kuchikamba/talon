package com.example.phoneboook.service.impl;


import com.example.phoneboook.dto.UserDto;
import com.example.phoneboook.entity.RoleEntity;
import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.entity.enums.ERole;
import com.example.phoneboook.exception.UserNotFoundException;
import com.example.phoneboook.repository.RoleRepository;
import com.example.phoneboook.repository.UserRepository;
import com.example.phoneboook.security.service.UserDetailsImpl;
import com.example.phoneboook.service.RoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto addRoleToUserByUserId(Long userId) throws Exception {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {

            Optional<UserEntity> optUser = userRepository.findById(userId);

            if(optUser.isPresent()) {
                UserEntity user = optUser.get();
                Set<RoleEntity> userRoles = user.getRoles();
                userRoles.add(roleRepository.findByName(ERole.ROLE_ADMIN).get());
                user.setRoles(userRoles);
                return UserDto.toUserDto(userRepository.save(user));
            } else {
                throw new UserNotFoundException("Не удалось найти пользователя!");
            }
        } else {
            throw new ResourceAccessException("У вас нет права доступа к данному ресурсу!");
        }
    }

    @Override
    public Set<RoleEntity> roles() throws Exception {

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            return new HashSet<>(roleRepository.findAll());
        } else {
            throw new ResourceAccessException("У вас нет права доступа к данному ресурсу!");
        }
    }

}
