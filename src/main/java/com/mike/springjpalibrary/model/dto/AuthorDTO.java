package com.mike.springjpalibrary.model.dto;

import com.mike.springjpalibrary.model.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Author")
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
