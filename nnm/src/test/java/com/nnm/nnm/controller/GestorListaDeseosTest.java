package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorListaDeseos;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.persistencia.ListaDeseosDAO;

@ExtendWith(MockitoExtension.class)
class GestorListaDeseosTest {

    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorUsuarios gestorUsuarios;
    @Mock private ListaDeseosDAO listaDeseosDAO;
    @InjectMocks private GestorListaDeseos gestor;

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
    }
}