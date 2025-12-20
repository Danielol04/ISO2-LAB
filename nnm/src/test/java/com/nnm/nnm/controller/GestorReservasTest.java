package com.nnm.nnm.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;

@ExtendWith(MockitoExtension.class)
class GestorReservasTest {

    @Mock
    private ReservaDAO reservaDAO;

    @InjectMocks
    private GestorReservas gestor;

    // -------------------------------------------------------------------------
    // 1. existeReserva
    // -------------------------------------------------------------------------

    @Test
    void testExisteReservaTrue() {
        Reserva r = new Reserva();
        when(reservaDAO.findById(1L)).thenReturn(r);

        assertTrue(gestor.existeReserva(1L));
    }

    @Test
    void testExisteReservaFalse() {
        when(reservaDAO.findById(1L)).thenReturn(null);

        assertFalse(gestor.existeReserva(1L));
    }

    // -------------------------------------------------------------------------
    // 2. registrarReserva
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarReserva() {
        Reserva r = new Reserva();

        gestor.registrarReserva(r);

        verify(reservaDAO).save(r);
    }

    // -------------------------------------------------------------------------
    // 3. actualizarReserva
    // -------------------------------------------------------------------------

    @Test
    void testActualizarReserva() {
        Reserva r = new Reserva();

        gestor.actualizarReserva(r);

        verify(reservaDAO).update(r);
    }

    // -------------------------------------------------------------------------
    // 4. autenticarReserva
    // -------------------------------------------------------------------------

    @Test
    void testAutenticarReserva() {
        Inquilino inq = new Inquilino();
        inq.setUsername("juanito");

        Reserva r = new Reserva();
        r.setInquilino(inq);

        when(reservaDAO.findById(1L)).thenReturn(r);

        assertTrue(gestor.autenticarReserva(1L, "juanito"));
        assertFalse(gestor.autenticarReserva(1L, "otro"));
    }

    @Test
    void testAutenticarReservaNoExiste() {
        when(reservaDAO.findById(1L)).thenReturn(null);

        assertFalse(gestor.autenticarReserva(1L, "juanito"));
    }

    // -------------------------------------------------------------------------
    // 5. cancelarReserva
    // -------------------------------------------------------------------------

    @Test
    void testCancelarReserva() {
        Reserva r = new Reserva();
        when(reservaDAO.findById(1L)).thenReturn(r);

        gestor.cancelarReserva(1L);

        verify(reservaDAO).delete(r);
    }

    // -------------------------------------------------------------------------
    // 6. obtenerReservaPorId
    // -------------------------------------------------------------------------

    @Test
    void testObtenerReservaPorId() {
        Reserva r = new Reserva();
        when(reservaDAO.findById(1L)).thenReturn(r);

        assertEquals(r, gestor.obtenerReservaPorId(1L));
    }

    // -------------------------------------------------------------------------
    // 7. obtenerReservasPorInmueble
    // -------------------------------------------------------------------------

    @Test
    void testObtenerReservasPorInmueble() {
        List<Reserva> lista = List.of(new Reserva(), new Reserva());
        when(reservaDAO.findReservasByInmuebleId(10L)).thenReturn(lista);

        assertEquals(lista, gestor.obtenerReservasPorInmueble(10L));
    }

    // -------------------------------------------------------------------------
    // 8. obtenerReservasPorInquilino
    // -------------------------------------------------------------------------

    @Test
    void testObtenerReservasPorInquilino() {
        List<Reserva> lista = List.of(new Reserva());
        when(reservaDAO.findReservasByInquilinoUsername("pepe")).thenReturn(lista);

        assertEquals(lista, gestor.obtenerReservasPorInquilino("pepe"));
    }

    // -------------------------------------------------------------------------
    // 9. obtenerReservasPorPropietario
    // -------------------------------------------------------------------------

    @Test
    void testObtenerReservasPorPropietario() {
        List<Reserva> lista = List.of(new Reserva());
        when(reservaDAO.findReservasByPropietarioUsername("prop1")).thenReturn(lista);

        assertEquals(lista, gestor.obtenerReservasPorPropietario("prop1"));
    }
}
