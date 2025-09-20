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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
/*
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findAll()
    {
        var authors = authorService.findAll();
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        authors.stream().map(aut -> new AuthorDTO(
                aut.getId(),
                aut.getNome(),
                aut.getBirthDate(),
                aut.getNationality()
        )).forEach(authorDTOs::add);
        return ResponseEntity.ok(authorDTOs);

    }*/

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> findById(@PathVariable String id)
    {
        var uuid = UUID.fromString(id);
        Optional<Author> author = authorService.findById(uuid);
        if (author.isPresent()) {
            AuthorDTO authorDTO = new AuthorDTO(
                    author.get().getId(),
                    author.get().getNome(),
                    author.get().getBirthDate(),
                    author.get().getNationality()
            );
            return ResponseEntity.ok().body(authorDTO);
        }
        return ResponseEntity.notFound().build();

    }

    // idempotente - mesmo retorno independentemente da repsota (not cool)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id)
    {
        var uuid = UUID.fromString(id);
        Optional<Author> author = authorService.findById(uuid);
        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

            authorService.delete(author.get());
            return ResponseEntity.noContent().build();


    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findByNameOrNationality(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "nationality", required = false) String nationality)
    {
        var authors = authorService.findByNameAndNationality(name, nationality);
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        authors.stream().map(aut -> new AuthorDTO(
                aut.getId(),
                aut.getNome(),
                aut.getBirthDate(),
                aut.getNationality()
        )).forEach(authorDTOs::add);
        return ResponseEntity.ok(authorDTOs);
    }



}