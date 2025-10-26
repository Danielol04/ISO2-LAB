package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inquilino;

@Repository
public class InquilinoDAO extends EntidadDAO<Inquilino, String> {

    public InquilinoDAO() {
        super(Inquilino.class);
    }
}