package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Inquilino;

@Repository
public class InquilinoDAO extends EntidadDAO<Inquilino, String> {

    public InquilinoDAO() {
        super(Inquilino.class);
    }

    public Inquilino findByUsername(String username) {
        String jpql = "FROM Inquilino e WHERE e.username = :username";
        return gestorBD.selectSingle(jpql,Inquilino.class, "username", username);
    }
}