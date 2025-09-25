package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.model.dto.BookDTO;
import com.mike.springjpalibrary.model.dto.ResponseErrorDTO;
import com.mike.springjpalibrary.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BookDTO>> findAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOS = books.stream().map(book -> {
                    AuthorDTO authorDTO = new AuthorDTO(
                            book.getAuthor().getId(),
                            book.getAuthor().getNome(),
                            book.getAuthor().getBirthDate(),
                            book.getAuthor().getNationality()
                    );
                    return new BookDTO(
                            book.getId(),
                            book.getIsbn(),
                            book.getDataPublicacao(),
                            book.getGenero(),
                            book.getPreco(),
                            authorDTO
                    );
                }
        ).collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BookDTO> findId(@PathVariable String id) {
        var uuid = UUID.fromString(id);
        Optional<Book> book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            AuthorDTO authorDTO = new AuthorDTO(
                    book.get().getAuthor().getId(),
                    book.get().getAuthor().getNome(),
                    book.get().getAuthor().getBirthDate(),
                    book.get().getAuthor().getNationality()
            );
            BookDTO bookDTO = new BookDTO(
                    book.get().getId(),
                    book.get().getIsbn(),
                    book.get().getDataPublicacao(),
                    book.get().getGenero(),
                    book.get().getPreco(),
                    authorDTO
            );


            return ResponseEntity.ok().body(bookDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {

        try {
            var uuid = UUID.fromString(id);
            var book = bookRepository.findById(uuid);
            if (book.isPresent()) {
                bookRepository.delete(book.get());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
        catch (OperationNotAllowed ex) {
            var error = ResponseErrorDTO.operationNotAllowed(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
    }



}