package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@Repository
public class PropietarioDAO extends EntidadDAO<Propietario, String> {

    public PropietarioDAO() {
        super(Propietario.class);
    }
}