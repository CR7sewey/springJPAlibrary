package com.mike.springjpalibrary.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface IService<T> {

    public T findById(UUID id) throws SQLException;
    public List<T> findAll();

}
