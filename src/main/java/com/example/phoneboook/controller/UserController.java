package com.example.phoneboook.controller;


import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.exception.BooksAlreadyAddToUser;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import com.example.phoneboook.service.UserService;
import com.example.phoneboook.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;



@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity getUsers() throws ResourceAccessException {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/{bookId}")
    public ResponseEntity addExistBookToUser(@PathVariable Long bookId) throws BooksAlreadyAddToUser, BookNotFoundException {
        return ResponseEntity.ok().body(userService.addExistBookToUser(bookId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity updateUserInfoByUserId(@PathVariable Long userId, @RequestBody UserEntity user) {
        return ResponseEntity.ok().body(userService.updateUserInfoByUserId(userId, user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserByUserId(@PathVariable Long userId) throws ResourceAccessException, UserNotFoundException {
        return ResponseEntity.ok().body(userService.deleteUserByUserId(userId));
    }
}
