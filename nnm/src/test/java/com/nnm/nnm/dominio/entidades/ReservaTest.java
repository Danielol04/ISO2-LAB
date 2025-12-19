package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class ReservaTest {

    @Test
    void testCalculosYEstado() {
        Inmueble inmueble = new Inmueble();
        inmueble.setPrecio_noche(100.0);
        
        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fin = LocalDate.now().plusDays(4);
        
        Reserva reserva = new Reserva(1L, inmueble, new Inquilino(), inicio, fin, PoliticaCancelacion.REEMBOLSABLE);
        
        // Test de lógica de fechas
        assertEquals(3, reserva.getNoches());
        assertEquals(300.0, reserva.getPrecioTotal());
        
        // Test de estado (No expirada aún)
        assertEquals(EstadoReserva.NOPAGADA, reserva.getEstado());
        
        // Test de estado expirado
        reserva.setFechaFin(LocalDate.now().minusDays(1));
        assertEquals(EstadoReserva.EXPIRADA, reserva.getEstado());
    }

    @Test
    void testSettersYGettersBasicos() {
        Reserva r = new Reserva();
        r.setId(10L);
        r.setPagado(true);
        r.setReservaDirecta(true);
        
        assertEquals(10L, r.getId());
        assertTrue(r.getPagado());
        assertTrue(r.getReservaDirecta());
    }
}
