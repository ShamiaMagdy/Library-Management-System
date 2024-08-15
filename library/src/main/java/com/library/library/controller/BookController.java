package com.library.library.controller;

import com.library.library.entity.Book;
import com.library.library.repository.BookRepository;
import com.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books= bookService.getAllBooks();
        return books.isEmpty()? ResponseEntity.noContent().build():ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        try {
            Book savedbook = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedbook);
        }catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(null);
    }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id){
        try{
            return ResponseEntity.ok(bookService.deleteBook(id));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

}
