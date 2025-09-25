package com.mike.springjpalibrary.controller.Mappers;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.dto.BookDTO;
import com.mike.springjpalibrary.model.dto.RegisterBookDTO;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.service.AuthorService;
import com.mike.springjpalibrary.service.BookService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public abstract class BooksMapper {

    @Autowired
    protected BookService bookService;
    @Autowired
    protected AuthorRepository authorRepository;

    // bcs form iddUser in dto I need to pass the Author with taht id!
    @Mapping(target = "author", expression = "java( authorRepository.findById(registerBookDTO.idUser()).orElse(null) )")
    //@Mapping(target = "titulo", source = "registerBookDTO.title")
    public abstract Book registerBook(RegisterBookDTO registerBookDTO);

    public abstract BookDTO searchBook(Book book);
}