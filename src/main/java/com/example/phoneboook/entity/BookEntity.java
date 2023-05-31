package com.example.phoneboook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Name;
    private String Phone;
    @ManyToMany(mappedBy = "Books", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserEntity> users = new ArrayList<>();

}
