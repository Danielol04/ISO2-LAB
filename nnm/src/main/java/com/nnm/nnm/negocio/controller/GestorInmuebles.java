package com.nnm.nnm.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorInmuebles {

    private final InmuebleDAO inmuebleDAO;
    private final GestorUsuarios gestorUsuarios;

    @Autowired
    public GestorInmuebles(InmuebleDAO inmuebleDAO, GestorUsuarios gestorUsuarios) {
        this.inmuebleDAO = inmuebleDAO;
        this.gestorUsuarios = gestorUsuarios;
    }   

    public void registrarInmueble(Inmueble inmueble) {
        inmuebleDAO.save(inmueble);
    }

    public Inmueble obtenerInmueblePorId(Long id) {
        return inmuebleDAO.findById(id);
    }

    public List<Inmueble> listarInmuebles() {
        return inmuebleDAO.findAll();
    }

    public List<Inmueble> listarInmueblesPorPropietario(String username) {
        return inmuebleDAO.findByPropietario(username);
    }
    public boolean eliminarInmueble(Long id, String usernamePropietario) {
        Inmueble inmueble = inmuebleDAO.findById(id);
        boolean existe = gestorUsuarios.esPropietario(usernamePropietario);
        if (inmueble != null && existe && inmueble.getPropietario().getUsername().equals(usernamePropietario) && sePuedeEliminarInmuebleSegunReservas(inmueble)) {
            inmuebleDAO.delete(inmueble);
            return true;
        }
        return false;
    }
    private boolean sePuedeEliminarInmuebleSegunReservas(Inmueble inmueble) {
        List<Reserva> reservas = inmueble.getReservas();
        if(reservas != null && !reservas.isEmpty()) {
            for(Reserva reserva : reservas){
                if(reserva.getEstado() != EstadoReserva.EXPIRADA) {
                    return false;
                }
            }
        }
        return true;
    }

}
    