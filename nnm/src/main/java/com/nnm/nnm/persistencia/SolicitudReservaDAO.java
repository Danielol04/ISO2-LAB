package com.nnm.nnm.persistencia;

import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

public class SolicitudReservaDAO extends EntidadDAO<SolicitudReserva,Long> {
    public SolicitudReservaDAO() {
        super(SolicitudReserva.class);
    }
}