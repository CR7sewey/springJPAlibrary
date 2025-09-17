package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/authors")
public class AuthorController
{
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) // bean gerenciado (service)
    {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<Void> saveAuthor(@RequestBody AuthorDTO authorDTO)
    {
        try {
            // Author - camada de persitencia; AuthorDTo - view
            var author = authorDTO.dtoToAuthor();
            authorService.save(author);
            // ex: .../author -> .../author/1
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(author.getId()).toUri(); // build new url with current one
            return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();

        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }

    }



}