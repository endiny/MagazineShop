package com.chernyak.dao;

import java.util.List;

/**
 * Generic DAO interface with CRUD methods
 */
public interface GenericDao<E> {
    void create(E entity);
    void delete(E entity);
    void update(E entity);
    E find(int id);
    List<E> findAll();
}
