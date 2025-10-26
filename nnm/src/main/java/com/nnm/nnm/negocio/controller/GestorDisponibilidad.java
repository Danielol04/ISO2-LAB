package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.persistencia.DisponibilidadDAO;

import java.util.List;

@Service
public class GestorDisponibilidad {

    @Autowired
    private DisponibilidadDAO disponibilidadDAO;

    public void registrarDisponibilidad(Disponibilidad d) {
        disponibilidadDAO.save(d);
    }

    public List<Disponibilidad> obtenerDisponibilidadPorInmueble(long id_inmueble) {
        return disponibilidadDAO.findByInmueble(id_inmueble);
    }
}
