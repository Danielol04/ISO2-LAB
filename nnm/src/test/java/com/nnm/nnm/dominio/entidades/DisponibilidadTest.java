package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

class DisponibilidadTest {

    @Test
    void testConstructorYSetters() {
        Inmueble i = new Inmueble();
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().plusMonths(1);
        
        Disponibilidad d = new Disponibilidad(i, inicio, fin, PoliticaCancelacion.NO_REEMBOLSABLE, true);
        
        assertEquals(i, d.getInmueble());
        assertEquals(inicio, d.getFechaInicio());
        assertTrue(d.getReservaDirecta());
        
        // Probar setters
        d.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);
        assertEquals(PoliticaCancelacion.REEMBOLSABLE, d.getPoliticaCancelacion());
    }
}
