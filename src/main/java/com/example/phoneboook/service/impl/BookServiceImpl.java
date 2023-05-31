package com.example.phoneboook.service.impl;


import com.example.phoneboook.dto.BookDto;
import com.example.phoneboook.entity.BookEntity;
import com.example.phoneboook.entity.UserEntity;
import com.example.phoneboook.exception.BookNotFoundException;
import com.example.phoneboook.exception.UserNotFoundException;
import com.example.phoneboook.repository.BookRepository;
import com.example.phoneboook.repository.UserRepository;
import com.example.phoneboook.security.service.UserDetailsImpl;
import com.example.phoneboook.service.BookService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<BookDto> books() throws ResourceAccessException {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ADMIN")) {
            return bookRepository.findAll().stream().map(BookDto::toBookDto).collect(Collectors.toList());
        } else {
            throw new ResourceAccessException("У вас нет права доступа к данному ресурсу!");
        }

    }

    @Override
    public List<BookDto> booksByUserId(Long userId) throws BookNotFoundException, UserNotFoundException {
        Optional<UserEntity> user = userRepository.findById(userId);
        if(user.isPresent()) {
            List<BookDto> bookDos = user.get().getBooks().stream().map(BookDto::toBookDto).toList();
            if (bookDos == null) {
                throw new BookNotFoundException("Задачи не найдены!");
            } else {
                return bookDos;
            }
        } else {
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден!");
        }

    }

    @Override
    public BookDto createBook(BookEntity book) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity user = userRepository.findById(userDetails.getId()).get();
        if(user == null) {
            throw new UserNotFoundException("Пользователь с id = " + userDetails.getId() + " не найден!");
        }

        try {
            List<BookEntity> userBooks = user.getBooks();
            userBooks.add(book);
            user.setBooks(userBooks);

            List<UserEntity> bookUsers = new ArrayList<>();
            bookUsers.add(user);
            book.setUsers(bookUsers);

            return BookDto.toBookDto(bookRepository.save(book));
        } catch (Exception e) {
            throw new Exception("Ошибка добавления номера!\n" + e.getMessage());
        }
    }

    @Override
    public BookDto phoneBook(Long bookId) throws Exception {
        Optional<BookEntity> optBook = bookRepository.findById(bookId);

        if (optBook.isPresent()) {
            BookEntity book = optBook.get();
            book.setPhone(!book.getPhone());
            return BookDto.toBookDto(bookRepository.save(book));
        } else {
            throw new BookNotFoundException("телефон не найден!");
        }
    }

    @Override
    public BookDto removeBook(Long bookId) throws Exception {
        Optional<BookEntity> optBook = bookRepository.findById(bookId);
        if (optBook.isPresent()) {
            BookEntity book = optBook.get();

            List<Long> usersIdForRemove = new ArrayList<>();
            book.getUsers().forEach(user -> {
                user.getBooks().remove(book);
            });

            usersIdForRemove.forEach(userId -> userRepository.deleteById(userId));

            BookEntity removedBook = bookRepository.findById(bookId).get();
            bookRepository.deleteById(bookId);

            return BookDto.toBookDto(removedBook);
        } else {
            throw new BookNotFoundException("Телефон не найден");
        }
    }
}

