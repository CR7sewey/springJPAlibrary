package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void saveAuthor() {
        Author author = new Author();
        author.setNome("MiguelDelete");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("Portuguese");
        var author1 = authorRepository.save(author);
        System.out.println(author1);
    }

    @Test
    public void updateAuthor() {
        var id = UUID.fromString("5ce335ec-0744-4ada-8092-fb127374ac48");

        var author = authorRepository.findById(id);
        if (author.isPresent()) {

            author.get().setId(id);
            author.get().setNome("Miguel 2");
            var author1 = authorRepository.save(author.get());
            System.out.println(author1);
        }
        else  {
            System.out.println("Author not found");
        }
    }

    @Test
    public void deleteAuthor() {

        var author = authorRepository.findByNome("MiguelDelete");
        if (author != null) {
            authorRepository.delete(author);
        }
        else  {
            System.out.println("Author not found");
        }
    }

    @Test
    public void findFirst10() {
        List<Author> authors = authorRepository.findAll();
        var printableList = authors.subList(1, Math.min(authors.size(), 10));
        printableList.forEach(System.out::println);
    }

    @Test
    public void saveAuthorWithBooks() { // cascade
        Author author = new Author();
        author.setNome("Miguel With Books");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("Portuguese");

        Book book = new Book();
        book.setDataPublicacao(LocalDate.of(2010,1,1));
        book.setGenero(Genero.MISTERIO);
        book.setIsbn("11111");
        book.setTitulo("Titulo do Book");
        book.setPreco(BigDecimal.valueOf(12));
        book.setAuthor(author);

        Book book2 = new Book();
        book2.setDataPublicacao(LocalDate.of(2010,1,1));
        book2.setGenero(Genero.BIOGRAFIA);
        book2.setIsbn("77777");
        book2.setTitulo("Titulo do Book");
        book2.setPreco(BigDecimal.valueOf(15));
        book2.setAuthor(author);

        author.getBooks().addAll(Arrays.asList(book2, book));
        authorRepository.save(author);
        //bookRepository.saveAll(author.getBooks()); // not needed bcs cascade


    }
}
