package com.example.phoneboook.service;

import com.example.phoneboook.dto.UserDto;
import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.exception.BooksAlreadyAddToUser;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers() throws ResourceAccessException;

    List updateUserInfoByUserId(Long userId, UserEntity userDetails);

    UserEntity deleteUserByUserId(Long userId) throws ResourceAccessException, UserNotFoundException;

    UserDto addExistBookToUser(Long bookId) throws ResourceAccessException, BookNotFoundException, BooksAlreadyAddToUser;
}
