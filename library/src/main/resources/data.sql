insert into Books (title, author, publication_Year, isbn) values ('The Great Gatsby', 'F. Scott Fitzgerald', '1925', '456789');
insert into Books (title, author, publication_Year, isbn) values ('To Kill a Mockingbird', 'Harper Lee', '1960', '123456');
insert into Books (title, author, publication_Year, isbn) values ('The Hobbit', 'J.R.R. Tolkien', '1937', '123569');

insert into Patrons (name, contact_information, phone_Number) values ('Alice Johnson', 'alice.johnson@example.com','01023456789');
insert into Patrons (name, contact_information, phone_Number) values ('Bob Brown', 'bob.brown@example.com','01123456789');
insert into Patrons (name, contact_information, phone_Number) values ('Marie Curie', 'marie.curie@example.com','01223456789');

INSERT INTO borrowing_records (book_id, patron_id, borrowing_date, return_date) VALUES (2, 2, '2024-08-14', NULL);
INSERT INTO borrowing_records (book_id, patron_id, borrowing_date, return_date) VALUES (3, 1, '2024-08-14', '2024-08-21');
INSERT INTO borrowing_records (book_id, patron_id, borrowing_date, return_date) VALUES (2, 3, '2024-08-14', '2024-08-21');
INSERT INTO borrowing_records (book_id, patron_id, borrowing_date, return_date) VALUES (1, 2, '2024-08-14', NULL);
INSERT INTO borrowing_records (book_id, patron_id, borrowing_date, return_date) VALUES (1, 1, '2024-08-14', NULL);
