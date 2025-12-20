package com.nnm.nnm.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.InmuebleDAO;

@ExtendWith(MockitoExtension.class)
class GestorInmueblesTest {

    @Mock private InmuebleDAO inmuebleDAO;
    @Mock private GestorUsuarios gestorUsuarios;
    @InjectMocks private GestorInmuebles gestor;

    // -------------------------------------------------------------------------
    // 1. registrarInmueble
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarInmueble() {
        Inmueble inm = new Inmueble();

        gestor.registrarInmueble(inm);

        verify(inmuebleDAO).save(inm);
    }

    // -------------------------------------------------------------------------
    // 2. obtenerInmueblePorId
    // -------------------------------------------------------------------------

    @Test
    void testObtenerInmueblePorId() {
        Inmueble inm = new Inmueble();
        when(inmuebleDAO.findById(5L)).thenReturn(inm);

        assertEquals(inm, gestor.obtenerInmueblePorId(5L));
    }

    // -------------------------------------------------------------------------
    // 3. listarInmuebles
    // -------------------------------------------------------------------------

    @Test
    void testListarInmuebles() {
        List<Inmueble> lista = List.of(new Inmueble(), new Inmueble());
        when(inmuebleDAO.findAll()).thenReturn(lista);

        assertEquals(lista, gestor.listarInmuebles());
    }

    // -------------------------------------------------------------------------
    // 4. listarInmueblesPorPropietario
    // -------------------------------------------------------------------------

    @Test
    void testListarInmueblesPorPropietario() {
        List<Inmueble> lista = List.of(new Inmueble());
        when(inmuebleDAO.findByPropietario("prop1")).thenReturn(lista);

        assertEquals(lista, gestor.listarInmueblesPorPropietario("prop1"));
    }

    // -------------------------------------------------------------------------
    // 5. eliminarInmueble (caso exitoso)
    // -------------------------------------------------------------------------

    @Test
    void testEliminarInmuebleExitoso() {
        Propietario prop = new Propietario();
        prop.setUsername("propietario1");

        Inmueble inmueble = mock(Inmueble.class);
        when(inmueble.getPropietario()).thenReturn(prop);
        when(inmueble.getReservas()).thenReturn(new ArrayList<>());

        when(inmuebleDAO.findById(1L)).thenReturn(inmueble);
        when(gestorUsuarios.esPropietario("propietario1")).thenReturn(true);

        boolean resultado = gestor.eliminarInmueble(1L, "propietario1");

        assertTrue(resultado);
        verify(inmuebleDAO).delete(inmueble);
    }

    // -------------------------------------------------------------------------
    // 6. eliminarInmueble (reservas activas)
    // -------------------------------------------------------------------------

    @Test
    void testNoEliminarSiTieneReservasActivas() {
        Propietario prop = new Propietario();
        prop.setUsername("propietario1");

        Reserva reservaActiva = new Reserva();
        reservaActiva.setFechaInicio(LocalDate.now().plusDays(1));
        reservaActiva.setFechaFin(LocalDate.now().plusDays(5));
        reservaActiva.setEstado(EstadoReserva.PAGADA);

        Inmueble inmueble = mock(Inmueble.class);
        when(inmueble.getPropietario()).thenReturn(prop);
        when(inmueble.getReservas()).thenReturn(List.of(reservaActiva));

        when(inmuebleDAO.findById(1L)).thenReturn(inmueble);
        when(gestorUsuarios.esPropietario("propietario1")).thenReturn(true);

        boolean resultado = gestor.eliminarInmueble(1L, "propietario1");

        assertFalse(resultado);
        verify(inmuebleDAO, never()).delete(any());
    }
}
