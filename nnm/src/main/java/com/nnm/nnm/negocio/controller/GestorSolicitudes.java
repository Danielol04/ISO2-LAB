package com.nnm.nnm.negocio.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;
import com.nnm.nnm.persistencia.ReservaDAO;
import com.nnm.nnm.persistencia.SolicitudReservaDAO;

@Service
public class GestorSolicitudes {
    @Autowired
    private SolicitudReservaDAO solicitudReservaDAO;
    @Autowired
    private ReservaDAO reservaDAO;
    @Autowired
    private GestorInmuebles gestorInmuebles;
    @Autowired
    private GestorReservas gestorReservas;

    public SolicitudReserva obtenerSolicitudPorId(long idSolicitud) {
        return solicitudReservaDAO.findById(idSolicitud);
    }

    public void generarSolicitudReserva(Reserva reserva, Double precioTotal) {
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setReserva(reserva);
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setPrecioTotal(precioTotal);
        long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        solicitud.setNoches(noches);
        solicitudReservaDAO.save(solicitud);
    }

    public void aceptarSolicitudReserva(long idSolicitud) {
        SolicitudReserva solicitud = solicitudReservaDAO.findById(idSolicitud);
        if (solicitud != null) {
            Reserva reserva = solicitud.getReserva();
            reserva.setEstado(EstadoReserva.ACEPTADA);
            reservaDAO.save(reserva);
            solicitudReservaDAO.delete(solicitud);
        }
    }

    public void rechazarSolicitudReserva(long idSolicitud) {
        SolicitudReserva solicitud = solicitudReservaDAO.findById(idSolicitud);
        if (solicitud != null) {
            solicitudReservaDAO.delete(solicitud);
            Reserva reserva = solicitud.getReserva();
            gestorReservas.cancelarReserva(reserva.getId());
        }
    }

    public List<SolicitudReserva> obtenerSolicitudesPorPropietario(String usernamePropietario) {
        List <SolicitudReserva> solicitudes= new java.util.ArrayList<>();
        List <Inmueble> inmueblesPropietario = gestorInmuebles.listarInmueblesPorPropietario(usernamePropietario);
        for (Inmueble inmueble : inmueblesPropietario) {
            List<SolicitudReserva> solicitudesInmueble = solicitudReservaDAO.findByInmueble(inmueble.getId());
            solicitudes.addAll(solicitudesInmueble);
        }
        return solicitudes;
    }
}