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

    public <T> List<T> select(String jpql, Class<T> entityClass) {
        try {
            return entityManager.createQuery(jpql, entityClass).getResultList();
        } catch (NoResultException exception) {
            return List.of();
        }
    }

    public <T> T selectSingle(String jpql, Class<T> entityClass, String paramName, Object value, Object... extras) {
        try {
            var query = entityManager.createQuery(jpql, entityClass);
            
            query.setParameter(paramName, value);
            
            if (extras != null && extras.length > 0) {
                for (int i = 0; i < extras.length; i += 2) {
                    if (i + 1 < extras.length) {
                        query.setParameter((String) extras[i], extras[i + 1]);
                    }
                }
            }
            
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }


    public <T> List<T> selectList(String jpql, Class<T> entityClass, String paramName, Object value) {
        try {
            return entityManager.createQuery(jpql, entityClass)
            .setParameter(paramName, value)
            .getResultList();
        } catch (NoResultException e) {
            return List.of();
        }
    
    }
    


    public <T> void insert(T entity) {
        entityManager.persist(entity);
    }

    public <T> T update(T entity) {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> void delete(T entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
    public <T> List<T> selectListConParametros(String jpql, Class<T> entityClass, String[] paramNames, Object[] values) {
        try {
            var query = entityManager.createQuery(jpql, entityClass);
            for (int i = 0; i < paramNames.length; i++) {
                query.setParameter(paramNames[i], values[i]);
            }
            return query.getResultList();
        } catch (NoResultException e) {
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
            .setParameter(string2, politicaCancelacion)
            .setParameter(string3, reservaDirecta)
            .setParameter(string4, fechaInicio)
            .setParameter(string5, fechaFin)
            .getResultList();
        } catch (NoResultException error) {
            return List.of();
        }
    }
}
