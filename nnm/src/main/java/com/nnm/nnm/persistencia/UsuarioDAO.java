package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Usuario;

@Repository
public class UsuarioDAO extends EntidadDAO<Usuario, String> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario findbyLogin(String Login){
        try{//Busca en la base de datos el usuario por su login
            return entityManager.createQuery("SELECT u FROM Usuario u WHERE u.username = :login", Usuario.class)
                .setParameter("login", Login)
                .getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}