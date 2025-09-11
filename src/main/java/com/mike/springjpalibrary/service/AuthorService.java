package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorService implements IService<Author> {


    private AuthorRepository repo;

    @Autowired
    public AuthorService(AuthorRepository repo) {
        this.repo = repo;
    }


    @Override
    public Author findById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = repo.findAll();
        return authors;
    }
}
