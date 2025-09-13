package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByAuthor(Author author);
    List<Book> findByTitulo(String Titulo);
    Book findByIsbn(String Isbn);
    List<Book> findByTituloAndPreco(String Title, BigDecimal preco);
}
