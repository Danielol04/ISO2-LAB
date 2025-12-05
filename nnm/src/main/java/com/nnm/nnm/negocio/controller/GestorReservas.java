package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;
import com.nnm.nnm.persistencia.ReservaDAO;
import com.nnm.nnm.persistencia.SolicitudReservaDAO;

@Service
public class GestorReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private SolicitudReservaDAO solicitudReservaDAO;

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

    public void generarSolicitudReserva(Reserva reserva) {
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setIdReserva(reserva.getId());
        solicitudReservaDAO.save(solicitud);
    }

    public Reserva obtenerReservaPorId(long idReserva) {
        return reservaDAO.findById(idReserva);
    }

    public void aceptarSolicitudReserva(long idSolicitud) {
        SolicitudReserva solicitud = solicitudReservaDAO.findById(idSolicitud);
        if (solicitud != null) {
            Reserva reserva = obtenerReservaPorId(idSolicitud);
            reserva.setAceptada(true);
            gestorDisponibilidad.actualizarDisponibilidadPorReserva(
                reserva.getInmueble().getId(),
                reserva.getFechaInicio(),
                reserva.getFechaFin()
            );
            reservaDAO.save(reserva);
            solicitudReservaDAO.delete(solicitud);
        }
    }

    public void rechazarSolicitudReserva(long idSolicitud) {
        SolicitudReserva solicitud = solicitudReservaDAO.findById(idSolicitud);
        if (solicitud != null) {
            solicitudReservaDAO.delete(solicitud);
        }
    }
}
