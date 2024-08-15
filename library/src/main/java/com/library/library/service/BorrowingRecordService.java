package com.library.library.service;

import com.library.library.entity.Book;
import com.library.library.entity.BorrowingRecord;
import com.library.library.entity.Patron;
import com.library.library.repository.BookRepository;
import com.library.library.repository.BorrowingRecordRepository;
import com.library.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BorrowingRecordService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PatronRepository patronRepository;
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    public Optional<BorrowingRecord> borrowBook(Long bookId, Long patronId){
        Optional<Book> book=bookRepository.findById(bookId);
        Optional<Patron> patron=patronRepository.findById(patronId);

        if(book.isEmpty() || patron.isEmpty()){
            return Optional.empty();
        }

        if(!book.get().getIsAvailable()){
            throw new IllegalArgumentException("Book is Already Borrowed.");
        }

        BorrowingRecord borrowingRecord=new BorrowingRecord();
        borrowingRecord.setBook(book.get());
        borrowingRecord.setPatron(patron.get());
        borrowingRecord.setBorrowingDate(LocalDate.now());

        book.get().setIsAvailable(false);
        bookRepository.save(book.get());

        return Optional.of(borrowingRecordRepository.save(borrowingRecord));

    }

    public Optional<BorrowingRecord> returnBook(Long bookId, Long patronId){
        Optional<BorrowingRecord> borrowingRecord=borrowingRecordRepository.findTopByBookIdAndPatronIdOrderByBorrowingDateDesc(bookId,patronId);

        if(borrowingRecord.isEmpty()){
            return Optional.empty();
        }

        borrowingRecord.get().setReturnDate(LocalDate.now());
        borrowingRecord.get().getBook().setIsAvailable(true);
        bookRepository.save(borrowingRecord.get().getBook());
        borrowingRecordRepository.save(borrowingRecord.get());

        return borrowingRecord;

    }


}
