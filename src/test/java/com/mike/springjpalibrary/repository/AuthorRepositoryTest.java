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

import java.time.LocalDate;

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

}
