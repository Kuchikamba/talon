package com.example.phoneboook.repository;


import com.example.phoneboook.entity.RoleEntity;
import com.example.phoneboook.entity.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
    Optional<RoleEntity> findById(Long id);
}

