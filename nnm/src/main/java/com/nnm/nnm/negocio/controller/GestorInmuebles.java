package com.nnm.nnm.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorInmuebles {

    @Autowired
    private InmuebleDAO inmuebleDAO;
    @Autowired
    private GestorUsuarios gestorUsuarios;

    public void registrarInmueble(Inmueble inmueble) {
        inmuebleDAO.save(inmueble);
    }

    public Inmueble obtenerInmueblePorId(Long id) {
        return inmuebleDAO.findById(id);
    }

    public List<Inmueble> listarInmuebles() {
        return inmuebleDAO.findAll();
    }

    public List<Inmueble> listarPorPropietario(String username) {
        return inmuebleDAO.findByPropietario(username);
    }
    public boolean eliminarInmueble(Long id, String usernamePropietario) {
        Inmueble inmueble = inmuebleDAO.findById(id);
        boolean existe = gestorUsuarios.esPropietario(usernamePropietario);
        if (inmueble != null && existe && inmueble.getPropietario().getUsername().equals(usernamePropietario)) {
            inmuebleDAO.delete(inmueble);
            return true;
        }
        return false;
    }

    public List<Inmueble> listarInmueblesPorPropietario(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
    