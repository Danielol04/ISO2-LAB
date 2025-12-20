package com.nnm.nnm.controller;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorListaDeseos;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;
import com.nnm.nnm.persistencia.ListaDeseosDAO;

@ExtendWith(MockitoExtension.class)
class GestorListaDeseosTest {

    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorUsuarios gestorUsuarios;
    @Mock private ListaDeseosDAO listaDeseosDAO;

    @InjectMocks private GestorListaDeseos gestor;

    // -------------------------------------------------------------------------
    // 1. Añadir inmueble (lista inexistente)
    // -------------------------------------------------------------------------

    @Test
    void testToggleInmuebleAñadir() {
        Inquilino inq = new Inquilino();
        Inmueble inm = new Inmueble();

        when(gestorUsuarios.obtenerInquilinoPorUsername("user1")).thenReturn(inq);
        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        boolean añadido = gestor.toggleInmueble(1L, "user1");

        assertTrue(añadido);
        assertNotNull(inq.getListaDeseos());
        assertTrue(inq.getListaDeseos().getInmuebles().contains(inm));
        verify(listaDeseosDAO).save(inq.getListaDeseos());
    }

    // -------------------------------------------------------------------------
    // 2. Quitar inmueble si ya estaba
    // -------------------------------------------------------------------------

    @Test
    void testToggleInmuebleQuitar() {
        Inquilino inq = new Inquilino();
        ListaDeseos lista = new ListaDeseos();
        lista.setInmuebles(new HashSet<>());

        Inmueble inm = new Inmueble();
        lista.getInmuebles().add(inm);

        inq.setListaDeseos(lista);

        when(gestorUsuarios.obtenerInquilinoPorUsername("user1")).thenReturn(inq);
        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        boolean resultado = gestor.toggleInmueble(1L, "user1");

        assertFalse(resultado);
        assertFalse(lista.getInmuebles().contains(inm));
        verify(listaDeseosDAO).save(lista);
    }

    // -------------------------------------------------------------------------
    // 3. Excepción si el inmueble no existe
    // -------------------------------------------------------------------------

    @Test
    void testToggleInmuebleInexistente() {
        Inquilino inq = new Inquilino();
        when(gestorUsuarios.obtenerInquilinoPorUsername("user1")).thenReturn(inq);
        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                gestor.toggleInmueble(1L, "user1")
        );
    }

    // -------------------------------------------------------------------------
    // 4. Añadir inmueble cuando la lista ya existe
    // -------------------------------------------------------------------------

    @Test
    void testToggleInmuebleAñadirConListaExistente() {
        Inquilino inq = new Inquilino();
        ListaDeseos lista = new ListaDeseos();
        lista.setInmuebles(new HashSet<>());

        inq.setListaDeseos(lista);

        Inmueble inm = new Inmueble();

        when(gestorUsuarios.obtenerInquilinoPorUsername("user1")).thenReturn(inq);
        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        boolean añadido = gestor.toggleInmueble(1L, "user1");

        assertTrue(añadido);
        assertTrue(lista.getInmuebles().contains(inm));
        verify(listaDeseosDAO).save(lista);
    }
}
