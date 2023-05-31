package com.example.phoneboook.service.impl;


import com.example.phoneboook.dto.UserDto;
import com.example.phoneboook.entity.BookEntity;
import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.exception.BooksAlreadyAddToUser;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import com.example.phoneboook.repository.BookRepository;
import com.example.phoneboook.repository.UserRepository;
import com.example.phoneboook.security.jwt.JwtUtils;
import com.example.phoneboook.security.service.UserDetailsImpl;
import com.example.phoneboook.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookRepository bookRepository;
    private final JwtUtils jwtUtils;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, BookRepository bookRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.bookRepository = bookRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public List<UserDto> getUsers() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            return userRepository.findAll().stream().map(UserDto::toUserDto).collect(Collectors.toList());
        } else {
            throw new ResourceAccessException("У вас нет доступа к данному ресурсу!");
        }
    }

    @Override
    public List updateUserInfoByUserId(Long userId, UserEntity changedUser) {

        UserEntity user = userRepository.findById(userId).orElseThrow(new UserNotFoundException("Пользователь не найден!"));

        if (!(changedUser.getUsername() == null)) {
            user.setUsername(changedUser.getUsername());
        }

        if (!(changedUser.getEmail() == null)) {
            user.setEmail(changedUser.getEmail());
        }

        if (!(changedUser.getPassword() == null)) {
            user.setPassword(passwordEncoder.encode(changedUser.getPassword()));
        }

        List lst = new ArrayList<>();
        lst.add(UserDto.toUserDto(userRepository.save(user)));
        lst.add(jwtUtils.generateJwtToken(new UserDetailsImpl(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList()))));

        return lst;
    }

    @Override
    public UserEntity deleteUserByUserId(Long userId) throws ResourceAccessException, UserNotFoundException {

        UserDetailsImpl securityUser = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (securityUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {

            Optional<UserEntity> optUser = userRepository.findById(userId);

            if (optUser.isPresent()) {
                UserEntity user = optUser.get();
                List<BookEntity> booksIdForRemove = new ArrayList<>();
                user.getBooks().forEach(book -> {
                    book.getUsers().remove(user);
                    bookRepository.save(book);
                    if (book.getUsers().size() == 0) {
                        booksIdForRemove.add(book);
                    }
                });

                bookRepository.deleteAll(booksIdForRemove);

                UserEntity removedUser = userRepository.findById(userId).get();
                userRepository.deleteById(userId);

                return removedUser;
            } else {
                throw new UserNotFoundException("Пользователь с id = " + userId + " не найден!");
            }
        } else {
            throw new ResourceAccessException("У вас нет доступа к данному ресурсу!");
        }
    }

    @Override
    public UserDto addExistBookToUser(Long bookId) throws BookNotFoundException, BooksAlreadyAddToUser {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BookEntity> optBook = bookRepository.findById(bookId);

        if(optBook.isPresent()) {
            BookEntity book = optBook.get();
            UserEntity user = userRepository.findById(userDetails.getId()).get();

            List<BookEntity> userBooks = user.getBooks();
            if(userBooks.contains(book)) {
                throw new BooksAlreadyAddToUser("Эта номер уже есть у данного пользователя!");
            } else {
                List<UserEntity> bookUsers = book.getUsers();

                userBooks.add(book);
                user.setBooks(userBooks);

                bookUsers.add(user);
                book.setUsers(bookUsers);

                return UserDto.toUserDto(userRepository.save(user));
            }
        } else {
            throw new BookNotFoundException("номер не найден!");
        }
    }
}
