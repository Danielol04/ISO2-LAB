package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DisponibilidadDAO {

    @PersistenceContext
    private EntityManager em;

    public void save(Disponibilidad d) {
        em.persist(d);
    }

    public List<Disponibilidad> findByInmueble(long id_inmueble) {
        String jpql = "FROM Disponibilidad d WHERE d.id_inmueble = :id";
        return em.createQuery(jpql, Disponibilidad.class)
                 .setParameter("id", id_inmueble)
                 .getResultList();
    }
}
