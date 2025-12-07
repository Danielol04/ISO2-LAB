package com.nnm.nnm.negocio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;

@Service
public class GestorReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    public boolean existeReserva(long id) {
        return reservaDAO.findById(id) != null;
    }

    // Registra una nueva reserva y actualiza la disponibilidad correspondiente
    public void registrarReserva(Reserva reserva) {
        reservaDAO.save(reserva);
    }

    public void actualizarReserva(Reserva reserva) {
        reservaDAO.update(reserva);
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

    public Reserva obtenerReservaPorId(long idReserva) {
        return reservaDAO.findById(idReserva);
    }

    public List<Reserva> obtenerReservasPorInmueble(long idInmueble) {
        return reservaDAO.findReservasByInmuebleId(idInmueble);
    }

    public List<Reserva> obtenerReservasPorInquilino(String usernameInquilino) {
        return reservaDAO.findReservasByInquilinoUsername(usernameInquilino);
    }

}