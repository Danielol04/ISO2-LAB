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
}
