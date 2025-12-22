package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;

class ListaDeseosTest {

    @Test
    void testAtributosYRelaciones() {
        ListaDeseos lista = new ListaDeseos();
        Inquilino inq = new Inquilino();
        Inmueble casa = new Inmueble();
        
        lista.setId(1L);
        lista.setInquilino(inq);
        lista.getInmuebles().add(casa);

        assertEquals(1L, lista.getId());
        assertEquals(inq, lista.getInquilino());
        assertEquals(1, lista.getInmuebles().size());
        assertTrue(lista.getInmuebles().contains(casa));
    }
}
