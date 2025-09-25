package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.BooksMapper;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.model.dto.BookDTO;
import com.mike.springjpalibrary.model.dto.RegisterBookDTO;
import com.mike.springjpalibrary.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements GeneralisedController {

    private final BookRepository bookRepository;
    private final BooksMapper booksMappingClass;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BookDTO>> findAll() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOS = books.stream()
                .map(booksMappingClass::searchBook)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookDTOS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BookDTO> findId(@PathVariable String id) {
        var uuid = UUID.fromString(id);
        Optional<Book> book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            /*AuthorDTO authorDTO = new AuthorDTO(
                    book.get().getAuthor().getId(),
                    book.get().getAuthor().getNome(),
                    book.get().getAuthor().getBirthDate(),
                    book.get().getAuthor().getNationality()
            );
            BookDTO bookDTO = new BookDTO(
                    book.get().getId(),
                    book.get().getIsbn(),
                    book.get().getTitulo(),
                    book.get().getDataPublicacao(),
                    book.get().getGenero(),
                    book.get().getPreco(),
                    authorDTO
            );*/
            BookDTO bookDTO = booksMappingClass.searchBook(book.get());


            return ResponseEntity.ok().body(bookDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {

        //  try {
        var uuid = UUID.fromString(id);
        var book = bookRepository.findById(uuid);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
        // }
      /*  catch (OperationNotAllowed ex) {
            var error = ResponseErrorDTO.operationNotAllowed(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
            }*/
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> saveBook(@RequestBody @Valid RegisterBookDTO bookDTO) {
        // try {
        Book book = booksMappingClass.registerBook(bookDTO);
            /*book.setTitulo(bookDTO.title());
            book.setIsbn(bookDTO.isbn());
            book.setDataPublicacao(bookDTO.dataPublicacao());
            book.setPreco(bookDTO.preco());
            book.setDataPublicacao(bookDTO.dataPublicacao());
            book.setGenero(bookDTO.genero());*/

        bookRepository.save(book);

        //URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(book.getId()).toUri(); // build new url with current one
        URI uri = generateURI(book.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
        // }
    /*    catch (DuplicateRegister ex) {
            var error = ResponseErrorDTO.conflictResponseErrorDTO(ex.getMessage());
            return ResponseEntity.status(error.status()).body(error);
        }
    }*/

    }

}