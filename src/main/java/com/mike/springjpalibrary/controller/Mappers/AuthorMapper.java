package com.mike.springjpalibrary.controller.Mappers;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author authorDTOToAuthor(AuthorDTO authorDTO);
    AuthorDTO authorToAuthorDTO(Author author);

}