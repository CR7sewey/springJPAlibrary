package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IService<T> {

    public T save(T t);

}
