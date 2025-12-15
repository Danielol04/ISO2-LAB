package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class InmuebleDAO {

    @PersistenceContext
    private EntityManager em;

    public void save(Inmueble inmueble) {
        em.persist(inmueble);
    }

    public Inmueble findById(Long id) {
        return em.find(Inmueble.class, id);
    }

    public List<Inmueble> findAll() {
        return em.createQuery("SELECT i FROM Inmueble i", Inmueble.class)
                 .getResultList();
    }

    public List<Inmueble> findByPropietario(String username) {
        return em.createQuery(
                "SELECT i FROM Inmueble i WHERE i.propietario.username = :username",
                Inmueble.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Inmueble> buscarFiltrado(
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

        var query = em.createQuery(jpql.toString(), Inmueble.class);

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
}
