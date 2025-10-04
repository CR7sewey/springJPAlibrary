package com.mike.springjpalibrary.model.dto;

import java.util.List;

public record UserDTO(
        String username,
        String password,
        List<String> roles
) {
}
