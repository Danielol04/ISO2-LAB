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

    // Consultar todas las disponibilidades
    public List<Disponibilidad> listarDisponibilidades() {
        return disponibilidadDAO.findAll();
    }

    // Consultar disponibilidad por Id
    public Disponibilidad obtenerDisponibilidad(long id) {
        return disponibilidadDAO.findById(id);
    }

    // Registrar o actualizar disponibilidad
    public void guardarDisponibilidad(Disponibilidad disponibilidad) {
        disponibilidadDAO.save(disponibilidad);
    }

    // Eliminar disponibilidad
    public void eliminarDisponibilidad(long id) {
        Disponibilidad d = disponibilidadDAO.findById(id);
        if(d != null) {
            disponibilidadDAO.delete(d);
        }
    }

    // Consultar disponibilidades por inmueble
    public List<Disponibilidad> listarPorInmueble(long idInmueble) {
        return listarDisponibilidades().stream()
                .filter(d -> d.getInmueble().getId() == idInmueble)
                .toList();
    }
}
