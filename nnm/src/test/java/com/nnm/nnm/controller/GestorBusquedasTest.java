package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.InmuebleDAO;

@ExtendWith(MockitoExtension.class)
class GestorBusquedasTest {

    @Mock
    private InmuebleDAO inmuebleDAO;

    @InjectMocks
    private GestorBusquedas gestorBusquedas;

    @Test
    void testBuscarLlamaAlDAOCorrectamente() {
        // Preparación (Given)
        List<Inmueble> listaEsperada = new ArrayList<>();
        listaEsperada.add(new Inmueble());
        
        // Configuramos el mock para que cuando se llame a buscarFiltrado con cualquier parámetro devuelva nuestra lista
        when(inmuebleDAO.buscarFiltrado(anyString(), anyInt(), anyInt(), anyDouble(), anyDouble()))
            .thenReturn(listaEsperada);

        // Ejecución (When)
        List<Inmueble> resultado = gestorBusquedas.buscar("Madrid", 2, 1, 50.0, 200.0);

        // Verificación (Then)
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        // Verificamos que el Gestor realmente usó el DAO con los parámetros que le pasamos
        verify(inmuebleDAO, times(1)).buscarFiltrado("Madrid", 2, 1, 50.0, 200.0);
    }
}