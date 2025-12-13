package com.nnm.nnm.persistencia;

import java.util.List;

import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

public class SolicitudReservaDAO extends EntidadDAO<SolicitudReserva,Long> {
    public SolicitudReservaDAO() {
        super(SolicitudReserva.class);
    }

     public List<SolicitudReserva> findByInmueble(long idInmueble) {
        String jpql = "FROM SolicitudReserva d WHERE d.reserva.inmueble.id = :idInmueble";
        return gestorBD.selectList(jpql, SolicitudReserva.class, "idInmueble", idInmueble);
    }
}