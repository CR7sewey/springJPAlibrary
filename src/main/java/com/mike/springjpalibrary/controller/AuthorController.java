package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.AuthorMapper;
import com.mike.springjpalibrary.controller.Mappers.UserMapper;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.security.SecurityService;
import com.mike.springjpalibrary.service.AuthorService;
import com.mike.springjpalibrary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j // log
@RestController
@RequestMapping("/authors")
@Tag(name = "Authors") // swagger
//@RequiredArgsConstructor - dependency injection without constructor set by us
public class AuthorController implements GeneralisedController
{
    private AuthorService authorService;
    private AuthorMapper authorMapper;
    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public AuthorController(AuthorService authorService, AuthorMapper authorMapper) // bean gerenciado (service)
    {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Save Author") // swagger
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author saved"),
            @ApiResponse(responseCode = "422", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Author already registered"),

    })
    public ResponseEntity<Object> saveAuthor(@RequestBody @Valid AuthorDTO authorDTO)//, Authentication authentication)
    {
        //try {
        // Author - camada de persitencia; AuthorDTo - view

        //User loggedUser = securityService.getLoggedUser();
        var author = authorMapper.authorDTOToAuthor(authorDTO);
        //author.setIdUser(loggedUser.getId());

        /*var user = (UserDetails) authentication.getPrincipal(); // UserDetail
        User user1 = userService.getByUsername(user.getUsername());
        var author = authorMapper.authorDTOToAuthor(authorDTO);
        author.setIdUser(user1.getId());*/
        authorService.save(author);
        log.info("Author saved: {}", author.getNome());
        // ex: .../author -> .../author/1
        //URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(author.getId()).toUri(); // build new url with current one
        URI uri = generateURI(author.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();


       /* catch (DuplicateRegister ex) {
            var error = ResponseErrorDTO.conflictResponseErrorDTO(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }*/
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
    @PreAuthorize("hasAnyRole('ADMIN', 'Admin', 'USER')")
    @Operation(summary = "Find Author") // swagger
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
    })
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Author") // swagger
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "400", description = "Author cannot be deleted - book registered"),

    })
    public ResponseEntity<Object> deleteById(@PathVariable String id)
    {
        // try {
        var uuid = UUID.fromString(id);
        Optional<Author> author = authorService.findById(uuid);
        if (author.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        authorService.delete(author.get());
        return ResponseEntity.noContent().build();
        //}
        /*catch (OperationNotAllowed ex) {
            var error = ResponseErrorDTO.operationNotAllowed(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }*/
    /*    catch (Exception ex) {
            var error = ResponseErrorDTO.standardResponseErrorDTO(ex.getMessage());
            System.out.println(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }*/

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'Admin', 'USER')")
    @Operation(summary = "Search Author") // swagger
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors found"),
    })
    public ResponseEntity<List<AuthorDTO>> findByNameOrNationality(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "birthDate", required = false) LocalDate birthDate, @RequestParam(value = "nationality", required = false) String nationality)
    {
        log.trace("findByNameOrNationality");
        log.debug("findByNameOrNationality");
        log.info("findByNameOrNationality");
        log.warn("findByNameOrNationality");
        var authors = authorService.findByExample(name, birthDate, nationality);
        List<AuthorDTO> authorDTOs = authors.stream().map(authorMapper::authorToAuthorDTO
        ).toList();

       /* AuthorDTO authorDTO = new AuthorDTO(
                authors.get().getId(),
                authors.get().getNome(),
                authors.get().getBirthDate(),
                authors.get().getNationality()
        );*/
        return ResponseEntity.ok(authorDTOs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update Author") // swagger
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author updated"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "409", description = "Author already registered"),

    })
    public ResponseEntity<Object> updateAuthor(@PathVariable String id, @RequestBody @Valid AuthorDTO authorDTO)
    {
        //  try {
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
        //    }
        /*catch (FieldsValidator ex) {
            var error = ResponseErrorDTO.unprocessableEntity(ex.getMessage(), ex.getFieldErrors());
            return ResponseEntity.status(error.status()).body(error);
        }*/
       /* catch (DuplicateRegister ex) {
            var error = ResponseErrorDTO.conflictResponseErrorDTO(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }*/
    /*    catch (Exception ex) {
            var error = ResponseErrorDTO.standardResponseErrorDTO(ex.getMessage());
            System.out.println(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }*/



    }



}