package com.nnm.nnm.dominio.entidades;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.MetodoPago;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class PagoTest {

    @Test
    void testConstructorVacio() {
        Pago pago = new Pago();

        assertNull(pago.getId());
        assertNull(pago.getReserva());
        assertNull(pago.getReferencia());
        assertNull(pago.getMetodoPago());
    }

    @Test
    void testConstructorConParametros() {
        Reserva reserva = new Reserva();
        UUID referencia = UUID.randomUUID();
        MetodoPago metodo = MetodoPago.TARJETA_CREDITO;

        Pago pago = new Pago(10L, reserva, referencia, metodo);

        assertEquals(10L, pago.getId());
        assertEquals(reserva, pago.getReserva());
        assertEquals(referencia, pago.getReferencia());
        assertEquals(metodo, pago.getMetodoPago());
    }

    @Test
    void testSettersYGetters() {
        Pago pago = new Pago();

        Reserva reserva = new Reserva();
        UUID referencia = UUID.randomUUID();
        MetodoPago metodo = MetodoPago.PAYPAL;

        pago.setId(5L);
        pago.setReserva(reserva);
        pago.setReferencia(referencia);
        pago.setMetodoPago(metodo);

        assertEquals(5L, pago.getId());
        assertEquals(reserva, pago.getReserva());
        assertEquals(referencia, pago.getReferencia());
        assertEquals(metodo, pago.getMetodoPago());
    }
}
