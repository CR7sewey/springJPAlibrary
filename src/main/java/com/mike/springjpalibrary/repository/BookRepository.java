package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
