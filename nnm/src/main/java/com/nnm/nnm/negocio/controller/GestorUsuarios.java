package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;
import com.nnm.nnm.negocio.dominio.entidades.Usuario;
import com.nnm.nnm.persistencia.InquilinoDAO;
import com.nnm.nnm.persistencia.PropietarioDAO;
import com.nnm.nnm.persistencia.UsuarioDAO;

@Service
public class GestorUsuarios {

    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private InquilinoDAO inquilinoDAO;
    @Autowired
    private PropietarioDAO propietarioDAO;

    public boolean autenticarUsuario(String username, String password) {
        Usuario usuario = usuarioDAO.findByUsername(username);
        return (usuario != null && usuario.getPassword().equals(password)); // True si el usuario existe y la contraseña coincide
    }
   
    // Verifica si un usuario existe en la base de datos
    public boolean existeUsuario(String username) {
        return usuarioDAO.findByUsername(username) != null; // True si el usuario existe, false si no
    }

    public void registrarInquilino(Inquilino inquilino) {
        inquilinoDAO.save(inquilino);
    }
    public void registrarPropietario(Propietario propietario) {
        propietarioDAO.save(propietario);
    }

}