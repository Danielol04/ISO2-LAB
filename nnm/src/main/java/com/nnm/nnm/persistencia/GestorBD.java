package com.nnm.nnm.persistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

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
        } catch (NoResultException _) {
            return List.of();
        }
    }

    // SELECT con parámetro (por ejemplo WHERE)
    public <T> T selectSingle(String jpql, Class<T> entityClass, String paramName, Object value) {
        try {
            return entityManager.createQuery(jpql, entityClass)
                .setParameter(paramName, value)
                .getSingleResult();
        } catch (NoResultException _) {
            return null;
        }   
    }
    // SELECT con tres parámetros (por ejemplo WHERE)
    public <T> T selectSingle(String jpql, Class<T> entityClass, String param1, Object value1, String param2, Object value2, String param3, Object value3) {
        try {
            return entityManager.createQuery(jpql, entityClass)
                .setParameter(param1, value1)
                .setParameter(param2, value2)
                .setParameter(param3, value3)
                .getSingleResult();
        } catch (NoResultException _) {
            return null;
        }   
    }


    // SELECT con parámetro (por ejemplo WHERE) 
    public <T> List<T> selectList(String jpql, Class<T> entityClass, String paramName, Object value) {
        try {
            return entityManager.createQuery(jpql, entityClass)
            .setParameter(paramName, value)
            .getResultList();
        } catch (NoResultException _) {
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
        } catch (Exception _) {
            return null;
        }
    }

    // DELETE genérico
    public <T> void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
    // Busqueda con varios parametros
    public <T> List<T> selectListConParametros(String jpql, Class<T> entityClass, String[] paramNames, Object[] values) {
        try {
            var query = entityManager.createQuery(jpql, entityClass);
            for (int i = 0; i < paramNames.length; i++) {
                query.setParameter(paramNames[i], values[i]);
            }
            return query.getResultList();
        } catch (NoResultException _) {
            return List.of();
        }
        }
        public List<Inmueble> buscarFiltradoInmuebles(
                String destino,
                Integer habitaciones,
                Integer banos,
                Double precioMin,
                Double precioMax
        ) {

            StringBuilder jpql = new StringBuilder("SELECT i FROM Inmueble i WHERE 1=1");

            if (destino != null && !destino.isBlank()) {
                jpql.append(" AND (LOWER(i.localidad) LIKE :destino OR LOWER(i.provincia) LIKE :destino)");
            }
            if (habitaciones != null) {
                jpql.append(" AND i.habitaciones >= :habitaciones");
            }
            if (banos != null) {
                jpql.append(" AND i.numero_banos >= :banos");
            }
            if (precioMin != null) {
                jpql.append(" AND i.precio_noche >= :precioMin");
            }
            if (precioMax != null) {
                jpql.append(" AND i.precio_noche <= :precioMax");
            }

            var query = entityManager.createQuery(jpql.toString(), Inmueble.class);

            if (destino != null && !destino.isBlank())
                query.setParameter("destino", "%" + destino.toLowerCase() + "%");
            if (habitaciones != null)
                query.setParameter("habitaciones", habitaciones);
            if (banos != null)
                query.setParameter("banos", banos);
            if (precioMin != null)
                query.setParameter("precioMin", precioMin);
            if (precioMax != null)
                query.setParameter("precioMax", precioMax);

            return query.getResultList();
        }

    public List<Disponibilidad> selectList(String jpql, Class<Disponibilidad> class1, String string, long idInmueble,
        String string2, PoliticaCancelacion politicaCancelacion, String string3,boolean reservaDirecta,String string4, LocalDate fechaInicio, String string5, LocalDate fechaFin) {
        try{
            return entityManager.createQuery(jpql, class1)
            .setParameter(string, idInmueble)
            .setParameter(string2, politicaCancelacion)//PoliticaCancelacion
            .setParameter(string3, reservaDirecta)
            .setParameter(string4, fechaInicio)
            .setParameter(string5, fechaFin)
            .getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    }
}
