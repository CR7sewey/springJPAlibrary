package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate dataPublicacao,
        Genero genero,
        BigDecimal preco,
        AuthorDTO author
) {

}