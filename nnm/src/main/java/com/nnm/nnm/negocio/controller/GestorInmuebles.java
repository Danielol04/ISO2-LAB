package com.nnm.nnm.negocio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorInmuebles {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    public void registrarInmueble(Inmueble inmueble) {
        inmuebleDAO.save(inmueble);
    }

    public List<Inmueble> listarInmuebles() {
        return inmuebleDAO.findAll();
    }

    public Inmueble obtenerInmueblePorId(long id) {
        return inmuebleDAO.findById(id);
    }

    public List<Inmueble> listarInmueblesPorPropietario(String propietarioUsername) {
        return inmuebleDAO.findByPropietario(propietarioUsername);
    }

    public List<Inmueble> buscarFiltrado(
            String localidad,
            String provincia,
            String titulo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String tipo,
            Integer habitaciones,
            Integer banos,
            Boolean reservaDirecta,
            PoliticaCancelacion politica
    ) {
        return inmuebleDAO.buscarFiltrado(
                localidad, provincia, titulo,
                fechaInicio, fechaFin,
                tipo, habitaciones, banos,
                reservaDirecta, politica
        );
    }
}
