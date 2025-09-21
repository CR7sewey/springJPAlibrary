package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AuthorRepository extends JpaRepository<Author, UUID> {


    List<Author> findByNome(String name);
    List<Author> findByNationality(String nationality);
    List<Author> findByBirthDate(LocalDate birthDate);

    List<Author> findByNationalityAndBirthDate(String nationality, LocalDate birthDate);
    List<Author> findByNomeAndBirthDate(String nome, LocalDate birthDate);
    List<Author> findByNomeAndNationality(String name, String nationality);

    List<Author> findByNomeAndBirthDateAndNationality(String name, LocalDate birthDate, String nationality);


}


