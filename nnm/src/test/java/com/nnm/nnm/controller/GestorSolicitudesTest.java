package com.nnm.nnm.controller;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;
import com.nnm.nnm.persistencia.SolicitudReservaDAO;

@ExtendWith(MockitoExtension.class)
class GestorSolicitudesTest {

    @Mock private SolicitudReservaDAO dao;
    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorReservas gestorReservas;

    @InjectMocks private GestorSolicitudes gestor;

    // -------------------------------------------------------------------------
    // 1. obtenerSolicitudPorId
    // -------------------------------------------------------------------------

    @Test
    void testObtenerSolicitudPorId() {
        SolicitudReserva sol = new SolicitudReserva();
        when(dao.findById(5L)).thenReturn(sol);

        assertEquals(sol, gestor.obtenerSolicitudPorId(5L));
    }

    // -------------------------------------------------------------------------
    // 2. obtenerSolicitudporIDreserva
    // -------------------------------------------------------------------------

    @Test
    void testObtenerSolicitudPorIDReserva() {
        SolicitudReserva sol = new SolicitudReserva();
        when(dao.findByIdReserva(10L)).thenReturn(sol);

        assertEquals(sol, gestor.obtenerSolicitudporIDreserva(10L));
    }

    // -------------------------------------------------------------------------
    // 3. generarSolicitudReserva
    // -------------------------------------------------------------------------

    @Test
    void testGenerarSolicitudReserva() {
        Reserva r = new Reserva();
        r.setFechaInicio(LocalDate.of(2025, 1, 1));
        r.setFechaFin(LocalDate.of(2025, 1, 5));

        gestor.generarSolicitudReserva(r, 200.0);

        verify(dao).save(argThat(sol ->
                sol.getReserva() == r &&
                sol.getPrecioTotal() == 200.0 &&
                sol.getNoches() == 4 &&
                sol.getFechaCreacion() != null
        ));
    }

    // -------------------------------------------------------------------------
    // 4. aceptarSolicitudReserva
    // -------------------------------------------------------------------------

    @Test
    void testAceptarSolicitud() {
        Reserva r = new Reserva();
        SolicitudReserva sol = new SolicitudReserva();
        sol.setReserva(r);

        r.setFechaInicio(LocalDate.of(2025, 1, 1));
        r.setFechaFin(LocalDate.now().plusDays(2));


        when(dao.findById(1L)).thenReturn(sol);

        gestor.aceptarSolicitudReserva(1L);

        assertEquals(EstadoReserva.ACEPTADA, r.getEstado());
        verify(gestorReservas).actualizarReserva(r);
        verify(dao).delete(sol);
    }

    @Test
    void testAceptarSolicitudNoExiste() {
        when(dao.findById(1L)).thenReturn(null);

        gestor.aceptarSolicitudReserva(1L);

        verify(gestorReservas, never()).actualizarReserva(any());
        verify(dao, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // 5. borrarSolicitudReserva
    // -------------------------------------------------------------------------

    @Test
    void testBorrarSolicitudReserva() {
        SolicitudReserva sol = new SolicitudReserva();

        gestor.borrarSolicitudReserva(sol);

        verify(dao).delete(sol);
    }

    // -------------------------------------------------------------------------
    // 6. obtenerSolicitudesPorPropietario
    // -------------------------------------------------------------------------

    @Test
    void testObtenerSolicitudesPorPropietario() {
        Inmueble i1 = mock(Inmueble.class);
        Inmueble i2 = mock(Inmueble.class);

        when(i1.getId()).thenReturn(100L);
        when(i2.getId()).thenReturn(200L);

        when(gestorInmuebles.listarInmueblesPorPropietario("pepe"))
                .thenReturn(List.of(i1, i2));

        SolicitudReserva s1 = new SolicitudReserva();
        SolicitudReserva s2 = new SolicitudReserva();
        SolicitudReserva s3 = new SolicitudReserva();

        when(dao.findByInmueble(100L)).thenReturn(List.of(s1, s2));
        when(dao.findByInmueble(200L)).thenReturn(List.of(s3));

        List<SolicitudReserva> resultado = gestor.obtenerSolicitudesPorPropietario("pepe");

        assertEquals(3, resultado.size());
        assertTrue(resultado.containsAll(List.of(s1, s2, s3)));
    }
}
