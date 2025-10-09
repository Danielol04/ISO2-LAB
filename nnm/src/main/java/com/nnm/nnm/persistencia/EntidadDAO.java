package com.nnm.nnm.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional //Indica que als operaciones tienen que ejecutarase dentro de una transacción de una base de datos
public abstract class EntidadDAO<T, ID> {
    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> entityClass;

    public EntidadDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(T entity) {
        return entityManager.merge(entity);
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }
}