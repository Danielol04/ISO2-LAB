package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.persistencia.PagoDAO;

@ExtendWith(MockitoExtension.class)
class GestorPagosTest {

    @Mock
    private PagoDAO pagoDAO;

    @InjectMocks
    private GestorPagos gestor;

    // -------------------------------------------------------------------------
    // 1. registrarPago
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarPago() {
        Pago p = new Pago();

        gestor.registrarPago(p);

        verify(pagoDAO).guardarPago(p);
    }

    // -------------------------------------------------------------------------
    // 2. obtenerPagoPorReserva
    // -------------------------------------------------------------------------

    @Test
    void testObtenerPagoPorReserva() {
        Pago pago = new Pago();
        when(pagoDAO.findByReservaID(10L)).thenReturn(pago);

        assertEquals(pago, gestor.obtenerPagoPorReserva(10L));
    }

    // -------------------------------------------------------------------------
    // 3. borrarPago
    // -------------------------------------------------------------------------

    @Test
    void testBorrarPago() {
        Pago p = new Pago();

        gestor.borrarPago(p);

        verify(pagoDAO).delete(p);
    }
}
