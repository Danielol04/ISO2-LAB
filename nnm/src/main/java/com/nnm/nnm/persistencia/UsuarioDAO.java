package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Usuario;

@Repository
public class UsuarioDAO extends EntidadDAO<Usuario, String> {

    public UsuarioDAO() {
        super(Usuario.class);
    }
}
