package com.nnm.nnm.dominio.entidades;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class InquilinoTest {

    @Test
    void testConstructorYAtributosPropios() {
        // Test del constructor con parámetros (usa super de Usuario)
        Inquilino inquilino = new Inquilino("saul_inq", "pass", "inq@test.com", "Saul", "Inq", "Calle A");
        
        assertEquals("saul_inq", inquilino.getUsername());

        // Test de ListaDeseos
        ListaDeseos lista = new ListaDeseos();
        inquilino.setListaDeseos(lista);
        assertEquals(lista, inquilino.getListaDeseos());
    }

    @Test
    void testGestionReservas() {
        Inquilino inquilino = new Inquilino();
        List<Reserva> listaReservas = new ArrayList<>();
        inquilino.setReservas(listaReservas);

        Reserva nuevaReserva = new Reserva();
        inquilino.añadirReserva(nuevaReserva);

        assertEquals(1, inquilino.getReservas().size());
        assertTrue(inquilino.getReservas().contains(nuevaReserva));
    }
}
