package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Author;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorDTO(
        UUID id,
        @NotBlank(message = "Required")
        @Size(max = 100, min = 1)
         String nome,
        @NotNull(message = "Required")
        @Past(message = "birthDate must be past")
        LocalDate birthDate,
        @NotBlank(message = "Required")
        @Size(max = 50, min = 1)
        String nationality
) {

}
