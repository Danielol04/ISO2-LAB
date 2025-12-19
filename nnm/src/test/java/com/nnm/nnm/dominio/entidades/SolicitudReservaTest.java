package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

class SolicitudReservaTest {

    @Test
    void testConstructorYGetters() {
        Reserva reserva = new Reserva();
        LocalDateTime ahora = LocalDateTime.now();
        SolicitudReserva solicitud = new SolicitudReserva(1L, reserva, 500.0, 5L, ahora);

        assertEquals(1L, solicitud.getId());
        assertEquals(reserva, solicitud.getReserva());
        assertEquals(500.0, solicitud.getPrecioTotal());
        assertEquals(5L, solicitud.getNoches());
        assertEquals(ahora, solicitud.getFechaCreacion());
        assertFalse(solicitud.getConfirmada()); // Valor por defecto
    }

    @Test
    void testSetters() {
        SolicitudReserva solicitud = new SolicitudReserva();
        solicitud.setConfirmada(true);
        solicitud.setPrecioTotal(120.0);
        solicitud.setNoches(2L);

        assertTrue(solicitud.getConfirmada());
        assertEquals(120.0, solicitud.getPrecioTotal());
        assertEquals(2L, solicitud.getNoches());
    }
}