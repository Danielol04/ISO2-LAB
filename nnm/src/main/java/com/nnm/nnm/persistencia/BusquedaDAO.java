package com.nnm.nnm.persistencia;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BusquedaDAO {

    private final EntityManager em;

    public BusquedaDAO(EntityManager em) {
        this.em = em;
    }

    public List<Inmueble> buscarInmuebles(String destino, int huespedes, int banos, int habitaciones,
                                          double precioMin, double precioMax) {
        String jpql = "SELECT i FROM Inmueble i " +
                      "WHERE (i.localidad LIKE :destino OR i.provincia LIKE :destino) " +
                      "AND i.numero_banos >= :banos " +
                      "AND i.habitaciones >= :habitaciones " +
                      "AND i.precio_noche BETWEEN :precioMin AND :precioMax";

        TypedQuery<Inmueble> query = em.createQuery(jpql, Inmueble.class);
        query.setParameter("destino", "%" + destino + "%");
        query.setParameter("banos", banos);
        query.setParameter("habitaciones", habitaciones);
        query.setParameter("precioMin", precioMin);
        query.setParameter("precioMax", precioMax);

        return query.getResultList();
    }
}
