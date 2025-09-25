package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService implements IService<Book> {

    private final BookRepository bookRepository;


    @Override
    public Book save(Book book) {
        return null;
    }

    @Override
    public Optional<Book> findById(UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void update(Book book) {

    }

}