package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface AuthorRepository extends JpaRepository<Author, UUID> {

    List<Author> findByNomeAndNationality(String name, String nationality);
    List<Author> findByNome(String name);
    List<Author> findByNationality(String nationality);



}


