package com.mike.springjpalibrary.model;

import com.mike.springjpalibrary.model.dto.AuthorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*; // spring-boot-start-test

public class AuthorTest {

    /*
    1. Cenario
    2. Execução
    3. Veificação
     */

    UUID id;
    String nome;
    String nationality;
    LocalDate data;
    Author author;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        nome = "Miguel";
        nationality = "Portuguese";
        data = LocalDate.now();
        author = new Author(
                id,
                nome,
                data,
                nationality
        );
    }

    @Test
    @DisplayName("Random author creation locally")
    void randomAuthorCreationLocally() {
        // 1


        // 2
        boolean nationalityFound = nationality.equals(author.getNationality());
        boolean nameFound = nome.equals(author.getNome());
        boolean birthDateFound = data.isEqual(author.getBirthDate());

        // 3
        System.out.println(author);
        /*Assertions.assertEquals("Portuguese", author.getNationality());
        Assertions.assertEquals("Miguel", author.getNome());
        Assertions.assertEquals(LocalDate.now(), author.getBirthDate());*/
        Assertions.assertNotNull(author);
        Assertions.assertTrue(nationalityFound);
        Assertions.assertTrue(nameFound);
        Assertions.assertTrue(birthDateFound);
        Assertions.assertTrue(author.equals(new Author(
                id,
                nome,
                data,
                nationality
        )));


        assertThat(author.getNationality()).isEqualTo(nationality);
        assertThat(author.getNome()).isEqualTo(nome);
        assertThat(author.getBirthDate()).isEqualTo(data);
    }

    @Test
    void exceptionLancada() {

        // Junit
        assertThrows(IllegalArgumentException.class, () -> new Author(
                UUID.randomUUID(),
                "",
                data,
                nationality
        ));

        // AssertJ
        var error = org.assertj.core.api.Assertions.catchException(() -> new Author(
                UUID.randomUUID(),
                "",
                data,
                nationality
        ));
        org.assertj.core.api.Assertions.assertThat(error)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Should not be empty.");

    }

    @Test
    void exceptionNotThrown() {

        assertDoesNotThrow(() -> new Author(
                UUID.randomUUID(),
                nome,
                data,
                nationality
        ));

    }

}
