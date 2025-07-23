package com.company.gym_system.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T save(T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void delete(Long id);
}
