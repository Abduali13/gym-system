package com.company.gym_system.dao;

import com.company.gym_system.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDao<T> implements GenericDao<T> {
    private final Map<Long, T> store = new ConcurrentHashMap<>();

    @Override 
    public T save(T e) {
        Long id = ReflectionUtils.getId(e);
        store.put(id, e);
        return e;
    }

    @Override 
    public Optional<T> findById(Long id) { 
        return Optional.ofNullable(store.get(id)); 
    }

    @Override 
    public List<T> findAll() { 
        return new ArrayList<>(store.values()); 
    }

    @Override 
    public void delete(Long id) { 
        store.remove(id); 
    }
}
