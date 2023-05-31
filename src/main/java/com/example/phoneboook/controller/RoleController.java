package com.example.phoneboook.controller;

import com.example.phoneboook.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity addRoleToUserByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok().body(roleService.addRoleToUserByUserId(userId));
    }

    @GetMapping("/")
    public ResponseEntity roles() throws Exception {
        return ResponseEntity.ok().body(roleService.roles());
    }
}
