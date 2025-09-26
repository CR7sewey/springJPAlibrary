package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BookSpecs {

    // https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html
    public static Specification<Book> isISBN(
            String isbn
    ) {
        // where ...
        return (root, query, builder) -> builder.equal(
                    root.get("isbn"), isbn);
    }

    public static Specification<Book> isTitleLike(
            String titulo
    ) {
        // where ...
        return (root, query, builder) -> builder.like(root.get("titulo"), "%" + titulo + "%");
    }

    public static Specification<Book> isGenero(
            Genero genero
    ) {
        // where ...
        return (root, query, builder) -> builder.equal(root.get("genero"), genero);
    }

    public static Specification<Book> isDataPublicacaoYear(
            LocalDate dataPublicacao
    ) {
        String year = String.valueOf(dataPublicacao.getYear());
        // select to_char(data_pulicacaom 'YYYY') from ...                                                                                     // func, return of func, property
        return (root, query, builder) -> builder.equal(builder.function("to_char", String.class, root.get("dataPublicacao"), builder.literal("YYYY")), year);
    }



}
