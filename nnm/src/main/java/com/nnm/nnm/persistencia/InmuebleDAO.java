package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

@Repository
public class InmuebleDAO extends EntidadDAO<Inmueble, Long> {

    public InmuebleDAO() {
        super(Inmueble.class);
    }

}