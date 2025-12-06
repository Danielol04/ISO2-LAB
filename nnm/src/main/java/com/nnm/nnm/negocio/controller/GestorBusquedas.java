package com.nnm.nnm.negocio.controller;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.BusquedaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class GestorBusquedas {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final BusquedaDAO dao;

    public GestorBusquedas() {
        emf = Persistence.createEntityManagerFactory("nombreUnidadPersistencia"); // Cambiar por tu PU
        em = emf.createEntityManager();
        dao = new BusquedaDAO(em);
    }

    public List<Inmueble> buscar(String destino, int huespedes, int banos, int habitaciones,
                                 double precioMin, double precioMax) {
        return dao.buscarInmuebles(destino, huespedes, banos, habitaciones, precioMin, precioMax);
    }

    public void cerrar() {
        em.close();
        emf.close();
    }
}
