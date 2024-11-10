 Books table
CREATE TABLE books (
    id NUMBER PRIMARY KEY,
    title VARCHAR2(255) NOT NULL,
    author VARCHAR2(255) NOT NULL,
    isbn VARCHAR2(13) NOT NULL UNIQUE,
    donated_by VARCHAR2(255) NOT NULL,
    is_available NUMBER(1) DEFAULT 1,
    donation_date DATE DEFAULT SYSDATE
);
-- Create sequence for books
CREATE SEQUENCE books_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
-- Create trigger for auto-incrementing book ID
CREATE OR REPLACE TRIGGER books_trigger
    BEFORE INSERT ON books
    FOR EACH ROW
BEGIN
    :NEW.id := books_seq.NEXTVAL;
END;
/
-- Users table
CREATE TABLE users (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    email VARCHAR2(255) NOT NULL UNIQUE,
    phone VARCHAR2(20),
    fines NUMBER(10,2) DEFAULT 0.00
);
-- Create sequence for users
CREATE SEQUENCE users_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
-- Create trigger for auto-incrementing user ID
CREATE OR REPLACE TRIGGER users_trigger
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    :NEW.id := users_seq.NEXTVAL;
END;
/
-- Borrowings table
CREATE TABLE borrowings (
    id NUMBER PRIMARY KEY,
    book_id NUMBER,
    user_id NUMBER,
    borrow_date DATE DEFAULT SYSDATE,
    due_date DATE,
    return_date DATE,
    fine_amount NUMBER(10,2) DEFAULT 0.00,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Create sequence for borrowings
CREATE SEQUENCE borrowings_seq
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
-- Create trigger for auto-incrementing borrowing ID
CREATE OR REPLACE TRIGGER borrowings_trigger
    BEFORE INSERT ON borrowings
    FOR EACH ROW
BEGIN
    :NEW.id := borrowings_seq.NEXTVAL;
END;
/
-- Create indexes

CREATE INDEX idx_borrowings_book ON borrowings(book_id);
CREATE INDEX idx_borrowings_user ON borrowings(user_id);

ALTER TABLE users ADD is_deleted NUMBER(1) DEFAULT 0;
ALTER TABLE books ADD is_deleted NUMBER(1) DEFAULT 0;
