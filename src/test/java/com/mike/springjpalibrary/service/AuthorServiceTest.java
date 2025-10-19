package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.repository.BookRepository;
import com.mike.springjpalibrary.security.SecurityService;
import com.mike.springjpalibrary.validator.AuthorValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepository authorRepository; // implamentacao fake - vazia!!!
    @Mock
    AuthorValidator authorValidator;
    @Mock
    BookRepository bookRepository;
    @Mock
    SecurityService securityService;

    @InjectMocks
    AuthorValidator authorValidator2;
    @InjectMocks
    AuthorService authorService; // injeta os mocks

    Author author;

    @BeforeEach
    public void setup() {
        author = new Author();
        author.setId(UUID.fromString("92d5d373-143d-4813-80a1-17c3bd3078b1"));
        author.setNome("Miguel");
        author.setNationality("Portuguese");
        author.setBirthDate(LocalDate.of(1999, 7, 7));
    }

    @Test
    void saveAuthor() {
        // need do define bcs mock is an empty implementation
        Mockito.when(authorRepository.save(Mockito.any())).thenReturn(author);

        var savedAuthor = authorService.save(author);
        Assertions.assertNotNull(savedAuthor);
        Assertions.assertEquals(author.getId(), savedAuthor.getId());

        Mockito.verify(authorRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void saveDuplicateRegister() {
        // when needs a not void return method, so I added the validateDuplicateRegister to  service (returns a boolean)
        Mockito.when(authorValidator.validar(Mockito.any())).thenThrow(new DuplicateRegister("Author already exists"));
        //Mockito.when(authorRepository.save(Mockito.any())).thenThrow(new DuplicateRegister("Author already exists"));
        var error = org.assertj.core.api.Assertions.catchException(() -> authorService.save(author));
        org.assertj.core.api.Assertions.assertThat(error)
                .isInstanceOf(DuplicateRegister.class)
                .hasMessage("Author already exists");
        Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any()); // method never called

    }

    @Test
    void findAuthorById() {
        // Mockito.mock(Author.class)
        Mockito.when(authorRepository.findById(Mockito.any())).thenReturn(Optional.of(author));
        var foundAuthor = authorService.findById(author.getId());
        Assertions.assertNotNull(foundAuthor);
        Assertions.assertEquals(author.getId(), foundAuthor.get().getId());
        Assertions.assertEquals(author.getNome(), foundAuthor.get().getNome());
        Assertions.assertEquals(author.getNationality(), foundAuthor.get().getNationality());
        Assertions.assertEquals(author.getBirthDate(), foundAuthor.get().getBirthDate());

        Mockito.verify(authorRepository, Mockito.times(1)).findById(Mockito.any());

    }

    @Test
    void findAll() {
        // Mockito.mock(Author.class)
        Mockito.when(authorRepository.findAll()).thenReturn(List.of(author));
        var foundAuthor = authorService.findAll();
        Assertions.assertNotNull(foundAuthor);
        Assertions.assertEquals(1, foundAuthor.size());

        Mockito.verify(authorRepository, Mockito.times(1)).findAll();

    }

    @Test
    void updateAuthor() {


        Mockito.when(authorRepository.findById(UUID.fromString("92d5d373-143d-4813-80a1-17c3bd3078b1"))).thenReturn(Optional.of(author));

        var author2 = new Author();
        author2.setId(UUID.fromString("92d5d373-143d-4813-80a1-17c3bd3078b1"));
        author2.setNome("Miguel 2");
        author2.setNationality("Portuguese");
        author2.setBirthDate(LocalDate.of(1999, 7, 7));

        Mockito.when(authorRepository.save(Mockito.any())).thenReturn(author2);

        authorService.update(author2);
        Mockito.when(authorRepository.findById(UUID.fromString("92d5d373-143d-4813-80a1-17c3bd3078b1"))).thenReturn(Optional.of(author2));
        var foundAuthor = authorRepository.findById(author2.getId());
        Assertions.assertNotNull(foundAuthor);
        Assertions.assertEquals(foundAuthor.get().getNome(), author2.getNome());
        Assertions.assertEquals(foundAuthor.get().getNationality(), author2.getNationality());
        Assertions.assertEquals(foundAuthor.get().getBirthDate(), author2.getBirthDate());

        Mockito.verify(authorRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void updateAuthorNotFound() {


        var author2 = new Author();
        author2.setNome("Miguel 2");
        author2.setNationality("Portuguese");
        author2.setBirthDate(LocalDate.of(1999, 7, 7));

        //Mockito.when(authorRepository.findById(Mockito.any())).thenReturn(Optional.empty()); - if I was doing this search in service this would be needed

        var error = org.assertj.core.api.Assertions.catchException(() -> authorService.update(author2));
        org.assertj.core.api.Assertions.assertThat(error)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author not registered");
       // Mockito.verify(authorRepository, Mockito.never()).save(Mockito.any()); // method never called


    }

    @Test
    void deleteAuthor() {

        authorService.delete(author);
        Mockito.verify(authorRepository, Mockito.times(1)).delete(author);

    }





}
