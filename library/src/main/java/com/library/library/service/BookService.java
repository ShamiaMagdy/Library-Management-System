package com.library.library.service;

import com.library.library.entity.Book;
import com.library.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(){
        List<Book> books=bookRepository.findAll();
        return books.isEmpty() ? Collections.emptyList() : books;
    }

    public Optional<Book> getBookById(Long Id) {
        Optional<Book> book=bookRepository.findById(Id);
        return Optional.of(book.orElse(new Book()));
    }

    public void validateBookData(Book book){
        if(book.getTitle() == null || book.getTitle().trim().isEmpty()){
            throw new IllegalArgumentException("Book Title Cannot Be Null.");
        }
        if(book.getAuthor() == null || book.getAuthor().trim().isEmpty()){
            throw new IllegalArgumentException("Book Author Cannot Be Null.");
        }
        if(book.getPublicationYear() == null || !book.getPublicationYear().matches("\\d{4}")){
            throw new IllegalArgumentException("Book Publication Year Must Be 4 Digits.");
        }
        if(book.getIsbn() == null || !book.getIsbn().matches("\\d{13,15}")){
            throw new IllegalArgumentException("Book ISBN Must Be A Number Between 13 To 15 Digits.");
        }
    }

    public Book addBook(Book book){
        validateBookData(book);
        if(bookRepository.findByTitle(book.getTitle()).isPresent()){
            throw  new IllegalArgumentException("This Book Title Already Exists.");
        }
        return bookRepository.save(book);
    }

    public Book updateBook(Long Id, Book updatedbook){
        return bookRepository.findById(Id)
                .map(book -> {
                    validateBookData(updatedbook);
                    book.setTitle(updatedbook.getTitle());
                    book.setAuthor(updatedbook.getAuthor());
                    book.setPublicationYear(updatedbook.getPublicationYear());
                    book.setIsbn(updatedbook.getIsbn());
                    book.setIsAvailable(updatedbook.getIsAvailable());
                    return bookRepository.save(book);
                }).orElseThrow(() -> new IllegalArgumentException("Book Id "+Id+" Not Found."));
    }

    public Boolean deleteBook(Long Id) {
        Optional<Book> book = bookRepository.findById(Id);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            System.out.println("Book deleted: " + book.get());
            return true;
        }
        System.out.println("Book not found with id: " + Id);
        return false;
    }



}
