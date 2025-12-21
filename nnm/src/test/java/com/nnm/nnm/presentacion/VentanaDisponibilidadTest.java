package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@ExtendWith(SpringExtension.class)
class VentanaDisponibilidadTest {

    @Mock private GestorDisponibilidad gestorDisponibilidad;
    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorReservas gestorReservas;

    @Mock private Model model;

    @InjectMocks private VentanaDisponibilidad controlador;

    // -------------------------------------------------------------------------
    // 1. Test de utilidad: generarListaFechas
    // -------------------------------------------------------------------------

    @Test
    void testGenerarListaFechas() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 3);

        List<String> fechas = controlador.generarListaFechas(inicio, fin);

        assertEquals(3, fechas.size());
        assertEquals("2025-01-01", fechas.get(0));
        assertEquals("2025-01-03", fechas.get(2));
    }

    // -------------------------------------------------------------------------
    // 2. Test eliminar: usuario no logueado
    // -------------------------------------------------------------------------

    @Test
    void testEliminarSinSesion() {
        MockHttpSession session = new MockHttpSession();

        String resultado = controlador.eliminar(1L, session);

        assertEquals("redirect:/login", resultado);
    }

    // -------------------------------------------------------------------------
    // 3. Test eliminar: disponibilidad no encontrada
    // -------------------------------------------------------------------------

    @Test
    void testEliminarDisponibilidadNoExiste() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorDisponibilidad.obtenerDisponibilidadPorId(1L)).thenReturn(null);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> controlador.eliminar(1L, session)
        );

        assertEquals(404, ex.getStatusCode().value());
    }

    // -------------------------------------------------------------------------
    // 4. Test crearDisponibilidad: fechas solapadas
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadSolapada() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();
        Propietario prop = new Propietario();
        prop.setUsername("juan");
        inm.setUsername_propietario(prop);

        Disponibilidad nueva = new Disponibilidad();
        nueva.setFechaInicio(LocalDate.of(2025, 1, 10));
        nueva.setFechaFin(LocalDate.of(2025, 1, 15));

        Disponibilidad existente = new Disponibilidad();
        existente.setFechaInicio(LocalDate.of(2025, 1, 12));
        existente.setFechaFin(LocalDate.of(2025, 1, 18));

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);
        when(gestorDisponibilidad.obtenerDisponibilidadPorInmueble(1L))
                .thenReturn(List.of(existente));

        String resultado = controlador.crearDisponibilidad(1L, nueva, session, model);

        assertEquals("redirect:/disponibilidades/crear/1", resultado);
    }

    // -------------------------------------------------------------------------
    // 5. Test crearDisponibilidad: fecha fin < fecha inicio
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadFechasInvalidas() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();
        Propietario prop = new Propietario();
        prop.setUsername("juan");
        inm.setUsername_propietario(prop);

        Disponibilidad nueva = new Disponibilidad();
        nueva.setFechaInicio(LocalDate.of(2025, 1, 15));
        nueva.setFechaFin(LocalDate.of(2025, 1, 10));

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        String resultado = controlador.crearDisponibilidad(1L, nueva, session, model);

        assertEquals("redirect:/disponibilidades/crear/1", resultado);
    }
}
