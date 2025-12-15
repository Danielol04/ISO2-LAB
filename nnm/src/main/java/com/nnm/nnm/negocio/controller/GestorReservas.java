package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;

@Service
public class GestorReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    public boolean existeReserva(long id) {
        return reservaDAO.findById(id) != null;
    }

    // Registra una nueva reserva y actualiza la disponibilidad correspondiente
    public void registrarReserva(Reserva reserva) {
        gestorDisponibilidad.actualizarDisponibilidadPorReserva(reserva.getInmueble().getId(), reserva.getFechaInicio(), reserva.getFechaFin());
        reservaDAO.save(reserva);
    }

    // Autentica si una reserva corresponde a un usuario específico
    public boolean autenticarReserva(long idReserva, String usernameUsuario) {
        Reserva reserva = reservaDAO.findById(idReserva);
        return reserva != null && reserva.getInquilino().getUsername().equals(usernameUsuario);
    }

    public boolean cancelarReserva(long idReserva, String usernameUsuario) {
        Reserva reserva = reservaDAO.findById(idReserva);
        if(reserva != null && reserva.getInquilino().getUsername().equals(usernameUsuario)){
            reservaDAO.delete(reserva);
            return true;
        }
        return false;
    }

}
