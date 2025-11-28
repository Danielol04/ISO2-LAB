package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

@Repository
public class SolicitudReservaDAO extends EntidadDAO<SolicitudReserva,Long> {
    public SolicitudReservaDAO() {
        super(SolicitudReserva.class);
    }
}