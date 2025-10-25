package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class GestorBD {

    @PersistenceContext
    private EntityManager entityManager;

    // SELECT genérico
    public <T> List<T> select(String jpql, Class<T> entityClass) {
        return entityManager.createQuery(jpql, entityClass).getResultList();
    }

    // SELECT con parámetro (por ejemplo WHERE)
    public <T> T selectSingle(String jpql, Class<T> entityClass, String paramName, Object value) {
        return entityManager.createQuery(jpql, entityClass)
                .setParameter(paramName, value)
                .getSingleResult();
    }

    // INSERT genérico
    public <T> void insert(T entity) {
        entityManager.persist(entity);
    }

    // UPDATE genérico
    public <T> T update(T entity) {
        return entityManager.merge(entity);
    }

    // DELETE genérico
    public <T> void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}
