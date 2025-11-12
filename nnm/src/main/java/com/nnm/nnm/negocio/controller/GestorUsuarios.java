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

    public String login(String username, String password) {
        Usuario usuario = usuarioDAO.findByUsername(username);
        String rol = "";
        if(usuario == null) return null; // No se encontro el usuario
        if(!usuario.getPassword().equals(password)) return null; // Contraseña incorrecta

        if(usuario instanceof Inquilino) {
            rol = "INQUILINO";
        } else if(usuario instanceof Propietario) {
            rol =  "PROPIETARIO";
        }
        return rol;
    }
    public boolean existeUsuario(String username) {
        return usuarioDAO.findByUsername(username) != null;
    }
    public void registrarInquilino(Inquilino inquilino) {
        inquilinoDAO.save(inquilino);
    }
    public void registrarPropietario(Propietario propietario) {
        propietarioDAO.save(propietario);
    }
    public Propietario obtenerPropietarioPorUsername(String usernamePropietario) {
        return propietarioDAO.findByUsername(usernamePropietario);
    }

    public Inquilino obtenerInquilinoPorUsername(String usernameInquilino) {
        return inquilinoDAO.findByUsername(usernameInquilino);
    }

    public boolean esPropietario(String username) {
        Usuario usuario = usuarioDAO.findByUsername(username);
        return usuario instanceof Propietario;
    }
}