package com.nnm.nnm.negocio.controller;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.GestorBD;

import java.util.List;

public class GestorBusquedas {

    private final GestorBD gestorBD;

    public GestorBusquedas() {
        this.gestorBD = new GestorBD();
    }

    /**
     * Busca inmuebles usando GestorBD directamente
     */
    public List<Inmueble> buscar(String destino, int huespedes, int banos, int habitaciones,
                                 double precioMin, double precioMax) {

        String jpql = "SELECT i FROM Inmueble i " +
                      "WHERE (i.localidad LIKE :destino OR i.provincia LIKE :destino) " +
                      "AND i.numero_banos >= :banos " +
                      "AND i.habitaciones >= :habitaciones " +
                      "AND i.precio_noche BETWEEN :precioMin AND :precioMax";

        // Usamos GestorBD con parámetros múltiples
        return gestorBD.selectListConParametros(jpql, Inmueble.class,
                new String[]{"destino","banos","habitaciones","precioMin","precioMax"},
                new Object[]{"%" + destino + "%", banos, habitaciones, precioMin, precioMax});
    }
}
