package com.library.library;
import com.library.library.entity.Book;
import com.library.library.repository.BookRepository;
import com.library.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        book.setPublicationYear("1925");
        book.setIsbn("9780743273565");
        book.setIsAvailable(true);
    }

    @Test
    void testGetAllBooks() {
        bookService.getAllBooks();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> foundBook = bookService.getBookById(1L);
        assertTrue(foundBook.isPresent());
        assertEquals("The Great Gatsby", foundBook.get().getTitle());
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Book> foundBook = bookService.getBookById(1L);
        assertFalse(!foundBook.isPresent());
    }

    @Test
    void testAddBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book savedBook = bookService.addBook(book);
        assertNotNull(savedBook);
        assertEquals("The Great Gatsby", savedBook.getTitle());
    }

    @Test
    void testUpdateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = new Book();
        updatedBook.setTitle("The Great Gatsby Updated");
        updatedBook.setAuthor("F. Scott Fitzgerald");
        updatedBook.setPublicationYear("1925");
        updatedBook.setIsbn("9780743273565");
        updatedBook.setIsAvailable(true);

        Optional<Book> result = Optional.ofNullable(bookService.updateBook(1L, updatedBook));

        assertTrue(result.isPresent());
        assertEquals("The Great Gatsby Updated", result.get().getTitle());
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Book updatedBook = new Book();
        updatedBook.setTitle("The Great Gatsby Updated");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBook(1L, updatedBook);
        });

        assertEquals("Book Id 1 Not Found.", exception.getMessage());
    }

    @Test
    void testDeleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).delete(book);

        boolean isDeleted = bookService.deleteBook(1L);
        assertTrue(isDeleted);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        boolean isDeleted = bookService.deleteBook(1L);
        assertFalse(isDeleted);
    }
}

