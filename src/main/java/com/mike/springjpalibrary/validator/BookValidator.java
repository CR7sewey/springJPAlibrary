package com.mike.springjpalibrary.validator;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.exceptions.FieldsValidator;
import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

@Component
public class BookValidator {

    private static final int year = 2020; // const

    @Autowired
    private BookRepository bookRepository;


    public void validate(Book book) {
        if (findDuplicateISBN(book)) {
            System.out.println("Duplicate ISBN ! ! !");
            throw new DuplicateRegister("ISBN duplicated - book already exists");
        }
        if (priceToDate(book)) {
            throw new FieldsValidator(
                    "Price needs to be set bcs year >= 2020", "Price"
            );
        }
    }


    public boolean findDuplicateISBN(Book book) {
        Optional<Book> b2 = bookRepository.findByIsbn(book.getIsbn());

        // post
        if (book.getId() == null) {
            return b2.isPresent();
        }

        // put - if !id but same isbn is an error; if same id, can have the same isbn to update fields
        return !b2.get().getId().equals(book.getId()); // b2.map(Book::getId).stream().anyMatch(id -> !id.equals(book.getId()));
    }

    // price mandatory if date > 2020
    public boolean priceToDate(Book book) {
        return book.getDataPublicacao().getYear() >= year && book.getPreco() == null;
    }

}