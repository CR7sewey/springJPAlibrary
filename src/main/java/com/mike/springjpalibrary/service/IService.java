package com.mike.springjpalibrary.service;

import java.util.List;
import java.util.UUID;

public interface IService<T> {

    public T findById(UUID id);
    public List<T> findAll();

}
