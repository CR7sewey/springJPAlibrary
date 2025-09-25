package com.mike.springjpalibrary.model.dto;

import org.springframework.http.HttpStatus;

import java.util.List;
// view /controller error
public record ResponseErrorDTO (
        Integer status,
        String message,
        List<FieldErrorDTO> fieldErrors
) {

    public static ResponseErrorDTO standardResponseErrorDTO(String message) {
        return new ResponseErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, List.of());
    }

    public static ResponseErrorDTO conflictResponseErrorDTO(String message) {
        return new ResponseErrorDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ResponseErrorDTO unprocessableEntity(String message, List<FieldErrorDTO> fieldErrors) {
        return new ResponseErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(), message, fieldErrors);
    }

    public static ResponseErrorDTO operationNotAllowed(String message) {
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

}