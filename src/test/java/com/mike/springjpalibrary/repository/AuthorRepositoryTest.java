package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest // sobe context da app spring boot so com a parte do JPA (o contexto de teste - isola)
@ActiveProfiles("test") // habilito profile de test (application-test)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Author creation")
    void createAuthor() {
        Author author = new Author();
        author.setNome("Miguel");
        author.setNationality("Portuguese");
        author.setBirthDate(LocalDate.of(1999, 7, 7));
        authorRepository.save(author);

        Assertions.assertNotNull(author.getId());
        System.out.println(author.getId());

    }

    @Test
    @Sql("/sql/seedAuthors.sql") // executa sempre antes do teste
    void updateAuthor() {
        List<Author> authors = authorRepository.findAll();
        var author0 = authors.stream().findFirst().get();

        author0.setNome("Miguel 2");
        var author = authorRepository.save(author0);
        Assertions.assertEquals(author.getNome(), "Miguel 2");

    }

    @Test
    void deleteAuthor() {
        Author author = new Author();
        author.setNome("Miguel");
        author.setNationality("Portuguese");
        author.setBirthDate(LocalDate.of(1999, 7, 7));
        authorRepository.save(author);

        authorRepository.delete(author);
        Assertions.assertFalse(authorRepository.findById(author.getId()).isPresent());
    }

    @Test
    void findAuthorById() {
        Author author = new Author();
        author.setNome("Miguel");
        author.setNationality("Portuguese");
        author.setBirthDate(LocalDate.of(1999, 7, 7));
        authorRepository.save(author);

        Optional<Author> optional = authorRepository.findById(author.getId());
        org.assertj.core.api.Assertions.assertThat(optional).isPresent();

    }


    @Test
    @Sql("/sql/seedAuthors.sql") // executa sempre antes do teste
    void findAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        authors.stream().forEach(System.out::println);
        Assertions.assertEquals(3, authors.size());

        var author0 = authors.stream().findFirst().get();
        Assertions.assertEquals("Miguel", author0.getNome());
        Assertions.assertEquals("Portuguese", author0.getNationality());
        Assertions.assertEquals(LocalDate.of(2025, 1, 1), author0.getBirthDate());
    }

}
