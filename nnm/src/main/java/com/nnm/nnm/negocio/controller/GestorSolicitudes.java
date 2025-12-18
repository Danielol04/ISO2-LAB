package com.nnm.nnm.negocio.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;
import com.nnm.nnm.persistencia.ReservaDAO;
import com.nnm.nnm.persistencia.SolicitudReservaDAO;
import com.nnm.nnm.presentacion.VentanaReserva;

@Service
public class GestorSolicitudes {
    
    private final SolicitudReservaDAO solicitudReservaDAO;
    private final GestorInmuebles gestorInmuebles;
    private final GestorReservas gestorReservas;

    @Autowired
    public GestorSolicitudes(SolicitudReservaDAO solicitudReservaDAO, GestorInmuebles gestorInmuebles, GestorReservas gestorReservas) {
        this.solicitudReservaDAO = solicitudReservaDAO;
        this.gestorInmuebles = gestorInmuebles;
        this.gestorReservas = gestorReservas;
    }

    public SolicitudReserva obtenerSolicitudPorId(long idSolicitud) {
        return solicitudReservaDAO.findById(idSolicitud);
    }

    public SolicitudReserva obtenerSolicitudporIDreserva(long idReserva){
        return solicitudReservaDAO.findByIdReserva(idReserva);
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
            gestorReservas.actualizarReserva(reserva);
            solicitudReservaDAO.delete(solicitud);
        }
    }

    public void borrarSolicitudReserva(SolicitudReserva solicitud) {
        solicitudReservaDAO.delete(solicitud);
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