package com.example.phoneboook.dto;



import com.example.phoneboook.entity.BookEntity;

public class BookDto {
    private Long id;
    private String name;
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookDto(String name, String phone, Long id) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public static BookDto toBookDto(BookEntity book) {
        return new BookDto(book.getName(), book.getPhone(), book.getId());
    }

}
