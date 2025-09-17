package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Author;

import java.time.LocalDate;

public record AuthorDTO(
        String nome,
        LocalDate birthDate,
        String nationality
) {

    public Author dtoToAuthor()
    {
        Author author = new Author();
        author.setNome(this.nome);
        author.setBirthDate(this.birthDate);
        author.setNationality(this.nationality);
        return author;
    }
}
