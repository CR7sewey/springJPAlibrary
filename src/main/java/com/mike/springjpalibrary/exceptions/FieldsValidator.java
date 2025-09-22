package com.mike.springjpalibrary.exceptions;

import com.mike.springjpalibrary.model.dto.FieldErrorDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FieldsValidator extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public FieldsValidator(String message) {
        super(message);
    }

}
