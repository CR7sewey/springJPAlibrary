package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// used to save a book
public record RegisterBookDTO(
        UUID id,
        @ISBN
        @NotBlank(message = "Mandatory field")
        String isbn,
        @NotBlank
        String titulo,
        @NotNull
        @PastOrPresent
        LocalDate dataPublicacao,
        Genero genero,
        BigDecimal preco,
        @NotNull
        UUID idUser
) {
}