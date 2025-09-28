package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.repository.BookRepository;
import com.mike.springjpalibrary.repository.BookSpecs;
import com.mike.springjpalibrary.validator.BookValidator;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService implements IService<Book> {

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;


    @Override
    public Book save(Book book) {
        bookValidator.validate(book);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(UUID id) {
        Optional<Book> book = bookRepository.findById(id);
        return book;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        return books;
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void update(Book book) {
        if (book.getId() == null) {
            throw new IllegalArgumentException("Book not registered");
        }
        bookValidator.validate(book);
        bookRepository.save(book);
    }

    public Page<Book> findBySpecification(
            String isbn,
            String titulo,
            String nomeAutor,
            Genero genero,
            LocalDate dataPublicacao,
            Integer numPage,
            Integer pageSize
    ) {
       /* Book book = new Book();
        book.setIsbn(isbn);
        book.setTitulo(titulo);
        book.setDataPublicacao(dataPublicacao);
        book.setGenero(genero);
        Author author = new Author();
        author.setNome(nomeAutor);
        book.setAuthor(author);

        ExampleMatcher ex = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase() // case insensitive
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Book> bookExample = Example.of(book, ex);
        return bookRepository.findAll(bookExample);*/

        Specification<Book> sp = Specification
                .allOf(((root, query, criteriaBuilder) -> criteriaBuilder.conjunction())); // 0 = 0 always true
               // .and(BookSpecs.isISBN(isbn))
               // .and(BookSpecs.isGenero(genero))
               // .and(BookSpecs.isTitleLike(titulo));
        if (isbn != null) {
            sp = sp.and(BookSpecs.isISBN(isbn));
        }
        if (genero != null) {
            sp = sp.and(BookSpecs.isGenero(genero));
        }
        if (titulo != null) {
            sp = sp.and(BookSpecs.isTitleLike(titulo));
        }
        if (dataPublicacao != null) {
            sp = sp.and(BookSpecs.isDataPublicacaoYear(dataPublicacao));
        }
        if (nomeAutor != null) {
            sp = sp.and(BookSpecs.isAuthorLike(nomeAutor));
        }

        // spring/jpa already implemented
        // if not, SELECT * FROM TableName ORDER BY id OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY; something like this
        Pageable page = PageRequest.of(numPage, pageSize);


        return bookRepository.findAll(sp, page);
    }


}