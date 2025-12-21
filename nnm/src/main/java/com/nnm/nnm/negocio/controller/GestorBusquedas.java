package com.nnm.nnm.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorBusquedas {

    private final InmuebleDAO inmuebleDAO;

    @Autowired
    public GestorBusquedas(InmuebleDAO inmuebleDAO) {
        this.inmuebleDAO = inmuebleDAO;
    }

    public List<Inmueble> buscar(
            String destino,
            Integer habitaciones,
            Integer banos,
            Double precioMin,
            Double precioMax
    ) {
        return inmuebleDAO.buscarFiltrado(destino, habitaciones, banos, precioMin, precioMax);
    }
}
