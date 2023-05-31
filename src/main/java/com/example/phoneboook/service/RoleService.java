package com.example.phoneboook.service;

import com.example.phoneboook.dto.UserDto;
import com.example.phoneboook.entity.RoleEntity;

import java.util.Set;

public interface RoleService {

    UserDto addRoleToUserByUserId(Long userId) throws Exception;

    Set<RoleEntity> roles() throws Exception;
}
