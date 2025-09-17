package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements IService<Author> {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) { // bean gerenciado (repository)
        this.authorRepository = authorRepository;
    }

    @Override
    public Author save(Author author){
            return authorRepository.save(author);
    }


}
