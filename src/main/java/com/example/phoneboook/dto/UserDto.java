package com.example.phoneboook.dto;



import com.example.phoneboook.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    private String username;
    private String email;
    private List<BookDto> books;
    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BookDto> getBooks() {
        return books;
    }

    public void setBooks(List<BookDto> books) {
        this.books = books;
    }

    public UserDto(String username, String email, List<BookDto> books, List<String> roles) {
        this.username = username;
        this.email = email;
        this.books = books;
        this.roles = roles;
    }

    public static UserDto toUserDto(UserEntity user) {
        return new UserDto(user.getUsername(), user.getEmail(), user.getBooks().stream().map(BookDto::toBookDto).collect(Collectors.toList()), user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()));
    }
}
