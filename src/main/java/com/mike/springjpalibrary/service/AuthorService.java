package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        authorRepository.delete(author);
    }

    @Override
    public Author update(Author author) {
        return null;
    }

    @Override
    public List<Author> findByNameAndNationality(String name, String nationality) {
        if(name == null && nationality == null) {
            return authorRepository.findAll();
        }

        if (name == null) {
            return authorRepository.findByNationality(nationality);
        }

        if(nationality == null) {
            return authorRepository.findByNome(name);
        }

        return authorRepository.findByNomeAndNationality(name, nationality);
    }


}
