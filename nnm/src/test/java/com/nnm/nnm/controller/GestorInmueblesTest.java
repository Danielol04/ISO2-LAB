package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate; // IMPORTANTE: Añade esta importación
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    void testNoEliminarSiTieneReservasActivas() {
        Propietario prop = new Propietario();
        prop.setUsername("propietario1");

        // CORRECCIÓN: Asignamos fechas para que el método getEstado() de Reserva no falle
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