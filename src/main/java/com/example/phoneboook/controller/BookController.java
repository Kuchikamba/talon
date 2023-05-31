package com.example.phoneboook.controller;

import com.example.phoneboook.entity.BookEntity;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import com.example.phoneboook.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public ResponseEntity books() throws ResourceAccessException {
        return ResponseEntity.ok().body(bookService.books());
    }

    @GetMapping("/{userId}")
    public ResponseEntity BooksByUserId(@PathVariable Long userId) throws BookNotFoundException, UserNotFoundException {
        return ResponseEntity.ok().body(bookService.booksByUserId(userId));
    }

    @PostMapping("/")
    public ResponseEntity createBook(@RequestBody BookEntity book) throws Exception {
        return ResponseEntity.ok().body(bookService.createBook(book));
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity phoneBook(@PathVariable Long bookId) throws Exception {
        return ResponseEntity.ok().body(bookService.phoneBook(bookId));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity removeBook(@PathVariable Long bookId) throws Exception {
        return ResponseEntity.ok().body(bookService.removeBook(bookId));
    }
}
