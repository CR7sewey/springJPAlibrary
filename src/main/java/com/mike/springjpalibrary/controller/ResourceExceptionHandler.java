package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.exceptions.OperationNotAllowed;
import com.mike.springjpalibrary.model.dto.FieldErrorDTO;
import com.mike.springjpalibrary.model.dto.ResponseErrorDTO;
import com.mike.springjpalibrary.validator.AuthorValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @Autowired
    public AuthorValidator authorValidator;

    // maniputlador global de excecoes no controller
    // sempre que der este o erro ele entra aqui
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // not needed bcs Im returning the repsonseentity and not the objetc Error
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        System.err.println(e.getFieldErrors().toString());
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldErrorDTO> fieldErrorDTOs = fieldErrors.stream().map(fe ->
                new FieldErrorDTO(
                        fe.getField(),
                        fe.getDefaultMessage()
                )
        ).collect(Collectors.toList());
        ResponseErrorDTO errorDTO = ResponseErrorDTO.unprocessableEntity("Validation failed", fieldErrorDTOs);
        return ResponseEntity.status(errorDTO.status()).body(errorDTO);
    }

    @ExceptionHandler(DuplicateRegister.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicateRegister(DuplicateRegister e, HttpServletRequest request){
        System.out.println("AQUUUUUUUUUUUUUUUUUUUUUi");
        ResponseErrorDTO errorDTO = ResponseErrorDTO.conflictResponseErrorDTO(e.getMessage());
        return ResponseEntity.status(errorDTO.status()).body(errorDTO);
    }

    @ExceptionHandler(OperationNotAllowed.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleOperationNotAllowed(DuplicateRegister e, HttpServletRequest request){
        System.out.println("AQUUUUUUUUUUUUUUUUUUUUUi 2");
        ResponseErrorDTO errorDTO = ResponseErrorDTO.operationNotAllowed(e.getMessage());
        return ResponseEntity.status(errorDTO.status()).body(errorDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleRunTimeException(DuplicateRegister e, HttpServletRequest request){
        System.out.println("AQUUUUUUUUUUUUUUUUUUUUUi 2");
        ResponseErrorDTO errorDTO = ResponseErrorDTO.standardResponseErrorDTO(e.getMessage());
        return ResponseEntity.status(errorDTO.status()).body(errorDTO);
    }

}