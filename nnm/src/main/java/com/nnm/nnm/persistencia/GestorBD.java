package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
@Transactional
public class GestorBD {

    @PersistenceContext
    private EntityManager entityManager;

    // SELECT genérico
    public <T> List<T> select(String jpql, Class<T> entityClass) {
        try {
            return entityManager.createQuery(jpql, entityClass).getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }

    // SELECT con parámetro (por ejemplo WHERE)
    public <T> T selectSingle(String jpql, Class<T> entityClass, String paramName, Object value) {
        try {
            return entityManager.createQuery(jpql, entityClass)
                .setParameter(paramName, value)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }   
    }

    // SELECT con parámetro (por ejemplo WHERE) 
    public <T> List<T> selectList(String jpql, Class<T> entityClass, String paramName, Object value) {
        try {
            return entityManager.createQuery(jpql, entityClass)
            .setParameter(paramName, value)
            .getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    
}


    // INSERT genérico
    public <T> void insert(T entity) {
        entityManager.persist(entity);
    }

    // UPDATE genérico
    public <T> T update(T entity) {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            return null;
        }
    }

    // DELETE genérico
    public <T> void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}
