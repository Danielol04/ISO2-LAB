package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

class InmuebleTest {

    @Test
    void testConstructorYGetters() {
        byte[] fotoFake = {1, 2, 3};
        Propietario prop = new Propietario();
        
        Inmueble i = new Inmueble(1L, prop, "Casa", "Apartamento", "Calle 1", "Madrid", "Madrid", "28001", 50.0, 1, 2, fotoFake);
        
        assertEquals("Casa", i.getTitulo());
        assertEquals(50.0, i.getPrecio_noche());
        assertArrayEquals(fotoFake, i.getFoto());
        assertEquals(prop, i.getPropietario());
    }

    @Test
    void testSettersYColecciones() {
        Inmueble i = new Inmueble();
        i.setLocalidad("Toledo");
        i.setHabitaciones(3);
        i.setDisponibilidades(new ArrayList<>());
        
        assertEquals("Toledo", i.getLocalidad());
        assertEquals(3, i.getHabitaciones());
        assertNotNull(i.getDisponibilidades());
    }
}
