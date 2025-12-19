package com.nnm.nnm.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;
import com.nnm.nnm.persistencia.SolicitudReservaDAO;

@ExtendWith(MockitoExtension.class)
class GestorSolicitudesTest {
    @Mock private SolicitudReservaDAO dao;
    @Mock private GestorReservas gestorReservas;
    @InjectMocks private GestorSolicitudes gestor;

    @Test
    void testAceptarSolicitud() {
        Reserva r = new Reserva();
        SolicitudReserva sol = new SolicitudReserva();
        sol.setReserva(r);
        
        when(dao.findById(1L)).thenReturn(sol);
        
        gestor.aceptarSolicitudReserva(1L);
        
        verify(gestorReservas).actualizarReserva(r);
        verify(dao).delete(sol);
    }
}