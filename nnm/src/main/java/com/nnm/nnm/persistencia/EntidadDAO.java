package com.nnm.nnm.persistencia;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.transaction.Transactional;

@Transactional
public abstract class EntidadDAO<T, ID> {

    @Autowired
    protected GestorBD gestorBD;

    private final Class<T> entityClass;

    protected EntidadDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(ID id) {
        String jpql = "FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id";
        return gestorBD.selectSingle(jpql, entityClass, "id", id);
    }

    public List<T> findAll() {
        return gestorBD.select("FROM " + entityClass.getSimpleName(), entityClass);
    }

    public void save(T entity) {
        gestorBD.insert(entity);
    }

    public T update(T entity) {
        return gestorBD.update(entity);
    }

    public void delete(T entity) {
        gestorBD.delete(entity);
    }
}