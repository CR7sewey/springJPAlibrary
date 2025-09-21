package com.mike.springjpalibrary.validator;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.exceptions.FieldsValidator;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.dto.FieldErrorDTO;
import com.mike.springjpalibrary.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// author validator!
@Component // bean gerenciado pelo spring
public class AuthorValidator {

    private AuthorRepository authorRepository;

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();


    @Autowired
    public AuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void validar(Author author) {
        System.out.println("Validating Author");
        if (validateDuplicateRegister(author)) {
            System.out.println("DUPLICATE REGISTER");
            throw new DuplicateRegister("Author already exists");
        }
        System.out.println(validateFieldRegister(author));
        if (validateFieldRegister(author)) {
            System.out.println("FIELD REGISTER");
            var fieldValidator = new FieldsValidator("Validation failed");
            fieldValidator.getFieldErrors().addAll(
                    fieldErrors
            );
            throw fieldValidator;

        }
    }


    public boolean validateDuplicateRegister(Author author) {
        System.out.println("Validating Duplicate Register");
        List<Author> exists = authorRepository.findByNomeAndBirthDateAndNationality(author.getNome(), author.getBirthDate(), author.getNationality());
        System.out.println("Existing Author found: " + exists);
        if (exists.isEmpty()) {
            return false;
        }

        if (author.getId() == null) {
            System.out.println(exists.getFirst());
            return exists.getFirst() != null;
        }
        return !author.getId().equals(exists.getFirst().getId());
    }

    public boolean validateFieldRegister(Author author) {
        fieldErrors.clear();
        boolean returnValue = false;
        if (author.getNome() == null) {
            returnValue = true;
            fieldErrors.add(new FieldErrorDTO("nome", "Name is required"));
        }
        if (author.getBirthDate() == null) {
            returnValue = true;
            fieldErrors.add(new FieldErrorDTO("birthDate", "Birthdate is required"));
        }
        if (author.getNationality() == null) {
            returnValue = true;
            fieldErrors.add(new FieldErrorDTO("nationality", "Nationality is required"));
        }
        return returnValue;
    }



}
