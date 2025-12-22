package com.nnm.nnm.dominio.entidades;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

class EnumsTest {

    @Test
    void testEnumPoliticaCancelacion() {
        assertNotNull(PoliticaCancelacion.valueOf("REEMBOLSABLE"));
        assertEquals(3, PoliticaCancelacion.values().length);
    }

    @Test
    void testEnumEstadoReserva() {
        assertNotNull(EstadoReserva.valueOf("PAGADA"));
        assertEquals(4, EstadoReserva.values().length);
    }
}
