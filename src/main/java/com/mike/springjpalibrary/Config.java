package com.mike.springjpalibrary;

import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config implements CommandLineRunner {

    @Autowired
    private AuthorService authorService;

    @Override
    public void run(String... args) throws Exception {

        var author = authorService.findAll();
        System.out.println("Author: " + author.getFirst());

    }


}
