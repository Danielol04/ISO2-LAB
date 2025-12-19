package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.MetodoPago;
import com.nnm.nnm.negocio.dominio.entidades.Pago;

class PagoTest {

    @Test
    void testConstructorYGetters() {
        UUID referencia = UUID.randomUUID();
        Pago pago = new Pago(1L, 100L, referencia, MetodoPago.PAYPAL);

        assertEquals(1L, pago.getId());
        assertEquals(100L, pago.getReserva());
        assertEquals(referencia, pago.getReferencia());
        assertEquals(MetodoPago.PAYPAL, pago.getMetodoPago());
    }

    @Test
    void testSetters() {
        Pago pago = new Pago();
        UUID nuevaRef = UUID.randomUUID();
        
        pago.setId(2L);
        pago.setReserva(200L);
        pago.setReferencia(nuevaRef);
        pago.setMetodoPago(MetodoPago.TARJETA_CREDITO);

        assertEquals(2L, pago.getId());
        assertEquals(200L, pago.getReserva());
        assertEquals(nuevaRef, pago.getReferencia());
        assertEquals(MetodoPago.TARJETA_CREDITO, pago.getMetodoPago());
    }
}
