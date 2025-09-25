package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Genero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// used to save a book
public record RegisterBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate dataPublicacao,
        Genero genero,
        BigDecimal preco,
        UUID idUser
) {
}