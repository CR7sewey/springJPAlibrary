package com.mike.springjpalibrary.exceptions;


// DOMAIN Exception - service
public class DuplicateRegister extends RuntimeException {

    public DuplicateRegister(String message) {
        super(message);
    }

}
