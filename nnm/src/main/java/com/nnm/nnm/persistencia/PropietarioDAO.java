package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@Repository
public class PropietarioDAO extends EntidadDAO<Propietario, String> {

    public PropietarioDAO() {
        super(Propietario.class);
    }

    public Propietario findByUsername(String username) {
        String jpql = "FROM Propietario e WHERE e.username = :username";
        return gestorBD.selectSingle(jpql, Propietario.class, "username", username);
    }
}