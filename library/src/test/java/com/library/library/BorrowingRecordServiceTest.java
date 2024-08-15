package com.library.library.service;

import com.library.library.entity.Book;
import com.library.library.entity.BorrowingRecord;
import com.library.library.entity.Patron;
import com.library.library.repository.BookRepository;
import com.library.library.repository.BorrowingRecordRepository;
import com.library.library.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BorrowingRecordServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

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

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");
        patron.setPhoneNumber("12345678901");
        patron.setContactInformation("john.doe@example.com");

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowingDate(LocalDate.now());
    }

    @Test
    void testBorrowBook_Success() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Optional<BorrowingRecord> result = borrowingRecordService.borrowBook(1L, 1L);

        assertTrue(result.isPresent(), "Expected a borrowing record to be returned");
        assertEquals(book, result.get().getBook(), "The borrowed book should match");
        assertEquals(patron, result.get().getPatron(), "The patron should match");
        assertFalse(book.getIsAvailable(), "The book should be marked as available");
    }

    @Test
    void testBorrowBook_BookNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        Optional<BorrowingRecord> result = borrowingRecordService.borrowBook(1L, 1L);

        assertFalse(result.isPresent(), "Expected no borrowing record to be returned");
    }

    @Test
    void testBorrowBook_PatronNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<BorrowingRecord> result = borrowingRecordService.borrowBook(1L, 1L);

        assertFalse(result.isPresent(), "Expected no borrowing record to be returned");
    }

    @Test
    void testBorrowBook_BookAlreadyBorrowed() {
        book.setIsAvailable(false); // Book is already borrowed
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            borrowingRecordService.borrowBook(1L, 1L);
        });

        assertEquals("Book is Already Borrowed.", thrown.getMessage(), "Exception message should match");
    }

    @Test
    void testReturnBook_Success() {
        borrowingRecord.setReturnDate(null);
        when(borrowingRecordRepository.findTopByBookIdAndPatronIdOrderByBorrowingDateDesc(anyLong(), anyLong())).thenReturn(Optional.of(borrowingRecord));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        Optional<BorrowingRecord> result = borrowingRecordService.returnBook(1L, 1L);

        assertTrue(result.isPresent(), "Expected a borrowing record to be returned");
        assertNotNull(result.get().getReturnDate(), "The return date should be set");
        assertTrue(book.getIsAvailable(), "The book should be marked as available");
    }

    @Test
    void testReturnBook_NotFound() {
        when(borrowingRecordRepository.findTopByBookIdAndPatronIdOrderByBorrowingDateDesc(anyLong(), anyLong())).thenReturn(Optional.empty());

        Optional<BorrowingRecord> result = borrowingRecordService.returnBook(1L, 1L);

        assertFalse(result.isPresent(), "Expected no borrowing record to be returned");
    }
}
