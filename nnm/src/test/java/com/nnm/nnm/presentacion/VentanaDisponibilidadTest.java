package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class VentanaDisponibilidadTest {

    @Mock private GestorDisponibilidad gestorDisponibilidad;
    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorReservas gestorReservas;
    @Mock private Model model;

    @InjectMocks private VentanaDisponibilidad controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -------------------------------------------------------------------------
    // 1. generarListaFechas
    // -------------------------------------------------------------------------

    @Test
    void testGenerarListaFechas() {
        List<String> fechas = controlador.generarListaFechas(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 3)
        );

        assertEquals(3, fechas.size());
        assertEquals("2025-01-01", fechas.get(0));
        assertEquals("2025-01-03", fechas.get(2));
    }

    // -------------------------------------------------------------------------
    // 2. GET /crear/{id} — usuario no logueado
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormularioSinSesion() {
        MockHttpSession session = new MockHttpSession();

        String resultado = controlador.mostrarFormulario(1L, model, session);

        assertEquals("redirect:/login", resultado);
    }

    // -------------------------------------------------------------------------
    // 3. GET /crear/{id} — disponibilidad expirada → se elimina
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormularioConDisponibilidadExpirada() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();
        Disponibilidad expirada = new Disponibilidad();
        expirada.setFechaInicio(LocalDate.now().minusDays(10));
        expirada.setFechaFin(LocalDate.now().minusDays(5));

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);
        when(gestorDisponibilidad.obtenerDisponibilidadPorInmueble(1L))
                .thenReturn(List.of(expirada));
        when(gestorReservas.obtenerReservasPorInmueble(1L))
                .thenReturn(List.of());

        String resultado = controlador.mostrarFormulario(1L, model, session);

        verify(gestorDisponibilidad).eliminarDisponibilidad(expirada);
        assertEquals("disponibilidad", resultado);
    }

    // -------------------------------------------------------------------------
    // 4. GET /crear/{id} — reserva expirada o no pagada → cancelar
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormularioConReservaExpirada() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();

        Reserva reserva = mock(Reserva.class);
        when(reserva.getEstado()).thenReturn(EstadoReserva.EXPIRADA);
        when(reserva.getId()).thenReturn(99L);

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);
        when(gestorDisponibilidad.obtenerDisponibilidadPorInmueble(1L))
                .thenReturn(List.of());
        when(gestorReservas.obtenerReservasPorInmueble(1L))
                .thenReturn(List.of(reserva));

        String resultado = controlador.mostrarFormulario(1L, model, session);

        verify(gestorReservas).cancelarReserva(99L);
        assertEquals("disponibilidad", resultado);
    }

    // -------------------------------------------------------------------------
    // 5. POST /crear/{id} — usuario no logueado
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadSinSesion() {
        MockHttpSession session = new MockHttpSession();
        Disponibilidad d = new Disponibilidad();

        String resultado = controlador.crearDisponibilidad(1L, d, session, model);

        assertEquals("redirect:/login", resultado);
    }

    // -------------------------------------------------------------------------
    // 6. POST /crear/{id} — inmueble no existe
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadInmuebleNoExiste() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                controlador.crearDisponibilidad(1L, new Disponibilidad(), session, model)
        );
    }

    // -------------------------------------------------------------------------
    // 7. POST /crear/{id} — usuario sin permiso
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadSinPermiso() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();
        Propietario otro = new Propietario();
        otro.setUsername("pepe");
        inm.setUsername_propietario(otro);

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        assertThrows(ResponseStatusException.class, () ->
                controlador.crearDisponibilidad(1L, new Disponibilidad(), session, model)
        );
    }

    // -------------------------------------------------------------------------
    // 8. POST /crear/{id} — creación correcta
    // -------------------------------------------------------------------------

    @Test
    void testCrearDisponibilidadCorrecta() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Inmueble inm = new Inmueble();
        Propietario p = new Propietario();
        p.setUsername("juan");
        inm.setUsername_propietario(p);

        Disponibilidad d = new Disponibilidad();

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        String resultado = controlador.crearDisponibilidad(1L, d, session, model);

        verify(gestorDisponibilidad).registrarDisponibilidad(d);
        assertEquals("redirect:/disponibilidades/crear/1", resultado);
    }

    // -------------------------------------------------------------------------
    // 9. POST /eliminar/{id} — usuario no logueado
    // -------------------------------------------------------------------------

    @Test
    void testEliminarSinSesion() {
        MockHttpSession session = new MockHttpSession();

        String resultado = controlador.eliminar(1L, session);

        assertEquals("redirect:/login", resultado);
    }

    // -------------------------------------------------------------------------
    // 10. POST /eliminar/{id} — disponibilidad no existe
    // -------------------------------------------------------------------------

    @Test
    void testEliminarDisponibilidadNoExiste() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorDisponibilidad.obtenerDisponibilidadPorId(1L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                controlador.eliminar(1L, session)
        );
    }

    // -------------------------------------------------------------------------
    // 11. POST /eliminar/{id} — usuario sin permiso
    // -------------------------------------------------------------------------

    @Test
    void testEliminarSinPermiso() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Disponibilidad d = new Disponibilidad();
        Inmueble inm = new Inmueble();
        Propietario p = new Propietario();
        p.setUsername("otro");
        inm.setUsername_propietario(p);
        d.setInmueble(inm);

        when(gestorDisponibilidad.obtenerDisponibilidadPorId(1L)).thenReturn(d);

        assertThrows(ResponseStatusException.class, () ->
                controlador.eliminar(1L, session)
        );
    }

    // -------------------------------------------------------------------------
    // 12. POST /eliminar/{id} — eliminación correcta
    // -------------------------------------------------------------------------

    @Test
    void testEliminarCorrecto() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        // Mock de inmueble con ID y propietario
        Inmueble inm = mock(Inmueble.class);
        Propietario p = new Propietario();
        p.setUsername("juan");

        when(inm.getPropietario()).thenReturn(p);
        when(inm.getId()).thenReturn(5L);

        // Disponibilidad real, pero con inmueble mockeado
        Disponibilidad d = new Disponibilidad();
        d.setInmueble(inm);

        when(gestorDisponibilidad.obtenerDisponibilidadPorId(1L)).thenReturn(d);

        String resultado = controlador.eliminar(1L, session);

        verify(gestorDisponibilidad).eliminarDisponibilidad(d);
        assertEquals("redirect:/disponibilidades/crear/5", resultado);
    }

}
