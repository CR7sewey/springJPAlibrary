package com.mike.springjpalibrary.controller.Mappers;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import org.springframework.stereotype.Component;

/*
@Component
public class AuthorMapperClass implements AuthorMapper {


    @Override
    public Author authorDTOToAuthor(AuthorDTO authorDTO) {
            Author author = new Author();
            author.setNome(authorDTO.nome());
            author.setBirthDate(authorDTO.birthDate());
            author.setNationality(authorDTO.nationality());
            return author;

    }

    @Override
    public AuthorDTO authorToAuthorDTO(Author author) {
        return new AuthorDTO(
                author.getId(),
                author.getNome(),
                author.getBirthDate(),
                author.getNationality()
        );
    }
}
*/