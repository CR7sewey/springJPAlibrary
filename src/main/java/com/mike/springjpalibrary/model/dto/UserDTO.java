package com.mike.springjpalibrary.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserDTO(
        @NotBlank
        String username,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String password,
        List<String> roles
) {
}
