package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.repository.BookRepository;
import com.mike.springjpalibrary.validator.AuthorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AuthorService implements IService<Author> {

    private final AuthorRepository authorRepository;
    private final AuthorValidator authorValidator;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, AuthorValidator authorValidator, BookRepository bookRepository) { // bean gerenciado (repository)
        this.authorRepository = authorRepository;
        this.authorValidator = authorValidator;
        this.bookRepository = bookRepository;
    }

    @Override
    public Author save(Author author){
        System.out.println("Saving Author");
        authorValidator.validar(author);
        return authorRepository.save(author);
    }

    @Override
    public Optional<Author> findById(UUID id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public void delete(Author author) {
        if (authorHasBook(author)) {
            throw new OperationNotAllowed("Author has books registerd");
        }
        authorRepository.delete(author);
    }

    @Override
    public void update(Author author) {
        if (author.getId() == null) {
            throw new IllegalArgumentException("Author not registered");
        }
        authorValidator.validar(author);
        authorRepository.save(author);
    }

    @Override
    public List<Author> findByNameAndBirthDateAndNationality(String name, LocalDate birthDate, String nationality) {
        if(name == null && nationality == null && birthDate == null) {
            return authorRepository.findAll();
        }

        if (name != null && nationality == null && birthDate == null) {
            return authorRepository.findByNome(name);
        }

        if (name == null && nationality != null && birthDate == null) {
            return authorRepository.findByNationality(nationality);
        }
        if (name == null && nationality == null) {
            return authorRepository.findByBirthDate(birthDate);
        }

        if (name == null) {
            return authorRepository.findByNationalityAndBirthDate(nationality, birthDate);
        }
        if (birthDate == null) {
            return authorRepository.findByNomeAndNationality(name, nationality);
        }
        if (nationality == null) {
            return authorRepository.findByNomeAndBirthDate(name, birthDate);
        }

        return authorRepository.findByNomeAndBirthDateAndNationality(name, birthDate, nationality);
    }

    public boolean authorHasBook(Author author) {
        return bookRepository.existsByAuthor(author);
    }

}
