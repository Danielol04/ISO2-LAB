package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;

@Repository
public class ReservaDAO extends EntidadDAO<Reserva, Long> {

    public ReservaDAO() {
        super(Reserva.class);
    }
}