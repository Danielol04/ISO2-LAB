package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;

@Service
public class GestorReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    // Verifica si una reserva existe en la base de datos por su ID
    public boolean existeReserva(long id) {
        return reservaDAO.findById(id) != null;
    }

    // Registra una nueva reserva
    public void registrarReserva(Reserva reserva) {
        reservaDAO.save(reserva);
    }

    // Autentica si una reserva corresponde a un usuario específico
    public boolean autenticarReserva(long idReserva, String usernameUsuario) {
        Reserva reserva = reservaDAO.findById(idReserva);
        return reserva != null && reserva.getUsername_inquilino().getUsername().equals(usernameUsuario);
    }

    // método para cancelar una reserva
    public boolean cancelarReserva(long idReserva, String usernameUsuario) {
        Reserva reserva = reservaDAO.findById(idReserva);
        if(reserva != null && reserva.getUsername_inquilino().getUsername().equals(usernameUsuario)){
            reservaDAO.delete(reserva);
            return true;
        }
        return false;
    }

}
