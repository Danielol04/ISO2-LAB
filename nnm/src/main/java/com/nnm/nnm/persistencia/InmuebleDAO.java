package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class InmuebleDAO {

    @PersistenceContext
    private EntityManager em;

    public void save(Inmueble i) {
        em.persist(i);
    }

    public List<Inmueble> findAll() {
        return em.createQuery("FROM Inmueble", Inmueble.class).getResultList();
    }

    public Inmueble findById(long id) {
        return em.find(Inmueble.class, id);
    }
}
