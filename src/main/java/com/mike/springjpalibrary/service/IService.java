package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IService<T> {

    public T save(T t);
    public Optional<T> findById(UUID id);
    public List<T> findAll();
    public void delete(T t);
    public T update(T t);
    public List<T> findByNameAndNationality(String name, String nationality);

}
