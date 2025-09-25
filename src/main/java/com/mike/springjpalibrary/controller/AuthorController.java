package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.AuthorMapper;
import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.exceptions.FieldsValidator;
import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.model.dto.ResponseErrorDTO;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.service.AuthorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/authors")
//@RequiredArgsConstructor - dependency injection without constructor set by us
public class AuthorController
{
    private AuthorService authorService;
    private AuthorMapper authorMapper;

    @Autowired
    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) // bean gerenciado (service)
    {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping
    public ResponseEntity<Object> saveAuthor(@RequestBody @Valid AuthorDTO authorDTO)
    {
        try {
            // Author - camada de persitencia; AuthorDTo - view
            var author = authorMapper.authorDTOToAuthor(authorDTO);
            authorService.save(author);
            // ex: .../author -> .../author/1
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(author.getId()).toUri(); // build new url with current one
            return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();

        }
        catch (DuplicateRegister ex) {
            var error = ResponseErrorDTO.conflictResponseErrorDTO(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
        /*catch (FieldsValidator ex) {
            var error = ResponseErrorDTO.unprocessableEntity(ex.getMessage(), ex.getFieldErrors());
            return ResponseEntity.status(error.status()).body(error);
        } */

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
        /*Optional<Author> author = authorService.findById(uuid);
        if (author.isPresent()) {
            AuthorDTO authorDTO = new AuthorDTO(
                    author.get().getId(),
                    author.get().getNome(),
                    author.get().getBirthDate(),
                    author.get().getNationality()
            );
            AuthorDTO authorDTO = authorMapper.authorToAuthorDTO(author.get());
            return ResponseEntity.ok().body(authorDTO);
        }
        return ResponseEntity.notFound().build();*/
        return authorService.findById(uuid)
                .map(author -> {
                    var dto = authorMapper.authorToAuthorDTO(author);
                    return ResponseEntity.ok(dto);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    // idempotente - mesmo retorno independentemente da repsota (not cool)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id)
    {
        try {
            var uuid = UUID.fromString(id);
            Optional<Author> author = authorService.findById(uuid);
            if (author.isEmpty()) {
                return ResponseEntity.notFound().build();

            }

            authorService.delete(author.get());
            return ResponseEntity.noContent().build();
        }
        catch (OperationNotAllowed ex) {
            var error = ResponseErrorDTO.operationNotAllowed(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
        catch (Exception ex) {
            var error = ResponseErrorDTO.standardResponseErrorDTO(ex.getMessage());
            System.out.println(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }

    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findByNameOrNationality(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "birthDate", required = false) LocalDate birthDate, @RequestParam(value = "nationality", required = false) String nationality)
    {
        var authors = authorService.findByExample(name, birthDate, nationality);
        List<AuthorDTO> authorDTOs = new ArrayList<>();
        authors.stream().map(authorMapper::authorToAuthorDTO
        ).collect(Collectors.toList());

       /* AuthorDTO authorDTO = new AuthorDTO(
                authors.get().getId(),
                authors.get().getNome(),
                authors.get().getBirthDate(),
                authors.get().getNationality()
        );*/
        return ResponseEntity.ok(authorDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAuthor(@PathVariable String id, @RequestBody @Valid AuthorDTO authorDTO)
    {
        try {
            var uuid = UUID.fromString(id);
            Optional<Author> author = authorService.findById(uuid);
            if (author.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            // automatically updated bcs entity state is managed ?
            author.get().setNome(authorDTO.nome());
            author.get().setBirthDate(authorDTO.birthDate());
            author.get().setNationality(authorDTO.nationality());
            System.out.println(author.get());
            authorService.update(author.get());
            return ResponseEntity.noContent().build(); // 204
        }
        /*catch (FieldsValidator ex) {
            var error = ResponseErrorDTO.unprocessableEntity(ex.getMessage(), ex.getFieldErrors());
            return ResponseEntity.status(error.status()).body(error);
        }*/
        catch (DuplicateRegister ex) {
            var error = ResponseErrorDTO.conflictResponseErrorDTO(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
        catch (Exception ex) {
            var error = ResponseErrorDTO.standardResponseErrorDTO(ex.getMessage());
            System.out.println(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }



    }



}