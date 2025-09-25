package com.mike.springjpalibrary.validator;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    @Autowired
    private BookRepository bookRepository;


    public void validate(Book book) {
        if (findDuplicateISBN(book)) {
            throw new DuplicateRegister("Book already exists");
        }
    }


    public boolean findDuplicateISBN(Book book) {
        var found = bookRepository.existsByIsbn(book.getIsbn());
        if (found) {
            return true;
        }
        return false;
    }

}