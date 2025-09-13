package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Test
    void saveBook() {
        Book book = new Book();
        book.setDataPublicacao(LocalDate.of(2010,1,1));
        book.setGenero(Genero.CIENCIA);
        book.setIsbn("123456789");
        book.setTitulo("Titulo do Book");
        book.setPreco(BigDecimal.valueOf(12));

        var author = authorRepository.findById(UUID.fromString("5ce335ec-0744-4ada-8092-fb127374ac48")).orElse(null);
        if (author!=null) {
            book.setAuthor(author);
        }
        bookRepository.save(book);

    }

    @Test
    void saveBookCascade() { // como cascade.ALL, it save automaticaly the new Author
        Book book = new Book();
        book.setDataPublicacao(LocalDate.of(2010,1,1));
        book.setGenero(Genero.CIENCIA);
        book.setIsbn("2134q532523");
        book.setTitulo("Titulo do Book");
        book.setPreco(BigDecimal.valueOf(12));

        Author author = new Author();
        author.setNome("Miguel Book");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("Portuguese");
        book.setAuthor(author);
        bookRepository.save(book);

    }

    @Test
    void getBookById() {
        var id = UUID.fromString("c5294463-70df-49d7-b1ad-4435726435a5");
        assertTrue(bookRepository.findById(id).isPresent());
    }

    @Test
    void updateAuthor() {
        var id = UUID.fromString("95a624fa-558c-4b14-aa38-06c71a6a2ca4");
        var book = bookRepository.findById(id).orElse(null);
        if (book!=null) {
            var idAuthor = UUID.fromString("5ce335ec-0744-4ada-8092-fb127374ac48");
            book.setAuthor(authorRepository.findById(idAuthor).orElse(null));
            bookRepository.save(book);
        }

    }

}