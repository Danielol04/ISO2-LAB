package com.nnm.nnm.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

    // -------------------------------------------------------------------------
    // 1. Verifica que el método llama al DAO con los parámetros correctos
    // -------------------------------------------------------------------------

    @Test
    void testBuscarLlamaAlDAOCorrectamente() {
        List<Inmueble> listaEsperada = new ArrayList<>();
        listaEsperada.add(new Inmueble());

        when(inmuebleDAO.buscarFiltrado(anyString(), anyInt(), anyInt(), anyDouble(), anyDouble()))
                .thenReturn(listaEsperada);

        List<Inmueble> resultado = gestorBusquedas.buscar("Madrid", 2, 1, 50.0, 200.0);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(inmuebleDAO, times(1))
                .buscarFiltrado("Madrid", 2, 1, 50.0, 200.0);
    }

    // -------------------------------------------------------------------------
    // 2. Caso adicional: el DAO devuelve lista vacía
    // -------------------------------------------------------------------------

    @Test
    void testBuscarListaVacia() {
        when(inmuebleDAO.buscarFiltrado(anyString(), anyInt(), anyInt(), anyDouble(), anyDouble()))
                .thenReturn(List.of());

        List<Inmueble> resultado = gestorBusquedas.buscar("Sevilla", 3, 2, 30.0, 150.0);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inmuebleDAO).buscarFiltrado("Sevilla", 3, 2, 30.0, 150.0);
    }
}
