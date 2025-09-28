package com.mike.springjpalibrary.exceptions;

import com.mike.springjpalibrary.model.dto.FieldErrorDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FieldsValidator extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();
    private String field;

    public FieldsValidator(String message, String field) {
        super(message);
        this.field = field;
    }

}
