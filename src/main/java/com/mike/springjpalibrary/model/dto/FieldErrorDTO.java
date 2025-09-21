package com.mike.springjpalibrary.model.dto;

import java.util.ArrayList;
import java.util.List;

public record FieldErrorDTO(
        String field,
        String error) {}

