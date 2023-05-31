package com.example.phoneboook.service;

import com.example.phoneboook.dto.BookDto;
import com.example.phoneboook.entity.BookEntity;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

public interface BookService {

    List<BookDto> books() throws ResourceAccessException;
    List<BookDto> booksByUserId(Long userId) throws BookNotFoundException, UserNotFoundException;
    BookDto createBook(BookEntity book) throws Exception;
    BookDto phoneBook(Long bookId) throws Exception;
    BookDto removeBook(Long bookId) throws Exception;
}
