package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.BooksMapper;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import com.mike.springjpalibrary.model.dto.BookDTO;
import com.mike.springjpalibrary.model.dto.RegisterBookDTO;
import com.mike.springjpalibrary.repository.BookRepository;
import com.mike.springjpalibrary.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements GeneralisedController {

    private final BookService bookService;
    private final BooksMapper booksMappingClass;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<BookDTO>> findAll(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false, value = "nome-autor") String nomeAutor,
            @RequestParam(required = false)Genero genero,
            @RequestParam(required = false, value = "ano-publicacao")LocalDate dataPublicacao,
            @RequestParam(required = false, defaultValue = "0", value = "page") Integer page,
            @RequestParam(required = false, defaultValue = "10", value = "page-size") Integer pageSize
            ) {
        Page<Book> p = bookService.findBySpecification(isbn, titulo, nomeAutor, genero, dataPublicacao, page, pageSize);
        /*List<BookDTO> bookDTOS = books.stream()
                .map(booksMappingClass::searchBook)
                .collect(Collectors.toList());*/

        Page<BookDTO> bookDTOS = p.map(booksMappingClass::searchBook);
        return ResponseEntity.ok(bookDTOS);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BookDTO> findId(@PathVariable String id) {
        var uuid = UUID.fromString(id);
        Optional<Book> book = bookService.findById(uuid);
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
        return bookService.findById(uuid).map(book ->
        {
            bookService.delete(book);
            return ResponseEntity.ok().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());
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

        bookService.save(book);

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

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateBook(@PathVariable String id, @RequestBody @Valid RegisterBookDTO bookDTO) {

        var uuid = UUID.fromString(id);
        Optional<Book> book = bookService.findById(uuid);
        if (book.isPresent()) {
            Book b = booksMappingClass.registerBook(bookDTO);
            book.get().setTitulo(b.getTitulo());
            book.get().setGenero(b.getGenero());
            book.get().setPreco(b.getPreco());
            book.get().setIsbn(b.getIsbn());
            book.get().setDataPublicacao(b.getDataPublicacao());
            book.get().setAuthor(b.getAuthor());


            bookService.update(book.get());
            return  ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();

       /* return bookService.findById(uuid)
                .map(book -> {
                            Book book2 = booksMappingClass.registerBook(bookDTO);
                            book2.setAuthor(book.getAuthor());
                            book2.setGenero(book.getGenero());
                            book2.setDataPublicacao(book.getDataPublicacao());
                            book2.setIsbn(book.getIsbn());
                            book2.setTitulo(book.getTitulo());
                            bookService.update(book2);
                            System.out.println("Book updated");
                            return ResponseEntity.noContent().build();
                        }
                )
                .orElseGet(() -> ResponseEntity.notFound().build());*/

    }

}