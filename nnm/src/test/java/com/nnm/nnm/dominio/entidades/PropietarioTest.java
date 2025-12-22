package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

class PropietarioTest {

    @Test
    void testConstructorYGetters() {
        Propietario prop = new Propietario("propietario1", "1234", "prop@test.com", "Juan", "Dueño", "Calle B");
        assertEquals("propietario1", prop.getUsername());
        assertEquals("Juan", prop.getNombre());
    }

    @Test
    void testGestionInmuebles() {
        Propietario prop = new Propietario();
        prop.setInmuebles(new ArrayList<>());

        Inmueble inmueble = new Inmueble();
        prop.añadirInmueble(inmueble);

        assertNotNull(prop.getInmuebles());
        assertEquals(1, prop.getInmuebles().size());
        assertEquals(inmueble, prop.getInmuebles().get(0));
    }
}
