package com.mike.springjpalibrary.controller.Mappers;

import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.dto.RegisterBookDTO;
import org.springframework.stereotype.Component;

/*
@Component
public class BooksMapperClass extends BooksMapper {

    @Override
    public Book registerBook(RegisterBookDTO registerBookDTO) {
        if (registerBookDTO == null) {
            return null;
        }
        var author = authorService.findById(registerBookDTO.idUser());
        if (!author.isPresent()) {
            return null;
        }

        Book book = new Book();
        book.setTitulo(registerBookDTO.title());
        book.setIsbn(registerBookDTO.isbn());
        book.setGenero(registerBookDTO.genero());
        book.setDataPublicacao(registerBookDTO.dataPublicacao());
        book.setPreco(registerBookDTO.preco());
        book.setAuthor(author.get());
        return book;
    }
}*/