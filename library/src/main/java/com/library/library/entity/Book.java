package com.library.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table (name = "Books")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", unique = true)
    private String title;

    private String author;
    private String publicationYear;
    private String isbn;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isAvailable;
}
