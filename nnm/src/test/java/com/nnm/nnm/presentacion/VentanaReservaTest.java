package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

class VentanaReservaTest {

    private MockMvc mockMvc;

    @Mock private GestorReservas gestorReservas;
    @Mock private GestorDisponibilidad gestorDisponibilidad;
    @Mock private GestorSolicitudes gestorSolicitudes;
    @Mock private GestorPagos gestorPagos;
    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorUsuarios gestorUsuarios;

    @InjectMocks private VentanaReserva controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setViewResolvers(resolver)
                .build();
    }

    // -------------------------------------------------------------------------
    // 1. GET /reserva/crear/{idInmueble}
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormularioReserva() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Disponibilidad disp = mock(Disponibilidad.class);
        when(disp.getFechaInicio()).thenReturn(LocalDate.of(2025, 1, 1));
        when(disp.getFechaFin()).thenReturn(LocalDate.of(2025, 1, 3));

        Reserva res = mock(Reserva.class);
        when(res.getEstado()).thenReturn(EstadoReserva.ACEPTADA);
        when(res.getPagado()).thenReturn(true);
        when(res.getFechaInicio()).thenReturn(LocalDate.of(2025, 1, 10));
        when(res.getFechaFin()).thenReturn(LocalDate.of(2025, 1, 12));

        Inmueble inm = mock(Inmueble.class);

        when(gestorDisponibilidad.obtenerDisponibilidadPorInmueble(10L)).thenReturn(List.of(disp));
        when(gestorReservas.obtenerReservasPorInmueble(10L)).thenReturn(List.of(res));
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inm);

        mockMvc.perform(get("/reserva/crear/10").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("reserva"))
                .andExpect(model().attributeExists("fechasDisponibles"))
                .andExpect(model().attributeExists("fechasReservadas"))
                .andExpect(model().attributeExists("inmueble"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /reserva/crear/{idInmueble} → usuario no válido
    // -------------------------------------------------------------------------

    @Test
    void testCrearReservaUsuarioNoValido_Propietario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "propietario");

        when(gestorUsuarios.esPropietario("propietario")).thenReturn(true);

        mockMvc.perform(post("/reserva/crear/10").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testCrearReservaUsuarioNoValido_SinSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/reserva/crear/10").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // -------------------------------------------------------------------------
    // 3. POST /reserva/crear/{idInmueble} → reserva directa
    // -------------------------------------------------------------------------

    @Test
    void testCrearReservaDirecta() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Reserva reserva = mock(Reserva.class);
        Inmueble inm = mock(Inmueble.class);
        when(inm.getPrecio_noche()).thenReturn(50.0);
        when(inm.getId()).thenReturn(10L);
        when(reserva.getFechaInicio()).thenReturn(LocalDate.of(2025, 1, 1));
        when(reserva.getFechaFin()).thenReturn(LocalDate.of(2025, 1, 3));
        when(reserva.getInmueble()).thenReturn(inm); // FIX clave

        Disponibilidad disp = mock(Disponibilidad.class);
        when(disp.getPoliticaCancelacion()).thenReturn(PoliticaCancelacion.REEMBOLSABLE);
        when(disp.getReservaDirecta()).thenReturn(true);

        when(gestorUsuarios.esPropietario("maria")).thenReturn(false);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inm);
        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(mock(Inquilino.class));
        when(gestorDisponibilidad.obtenerDisponibilidadParaReserva(eq(10L), any(), any()))
                .thenReturn(List.of(disp));
        when(reserva.getId()).thenReturn(99L);

        mockMvc.perform(post("/reserva/crear/10")
                        .session(session)
                        .flashAttr("reserva", reserva))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pago/confirmarPago/99"));

        verify(gestorReservas).registrarReserva(reserva);
    }

    // -------------------------------------------------------------------------
    // 4. POST /reserva/crear/{idInmueble} → reserva NO directa
    // -------------------------------------------------------------------------

    @Test
    void testCrearReservaNoDirecta() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Reserva reserva = mock(Reserva.class);
        Inmueble inm = mock(Inmueble.class);
        when(inm.getPrecio_noche()).thenReturn(100.0);
        when(inm.getId()).thenReturn(10L);
        when(reserva.getFechaInicio()).thenReturn(LocalDate.of(2025, 1, 1));
        when(reserva.getFechaFin()).thenReturn(LocalDate.of(2025, 1, 3));
        when(reserva.getInmueble()).thenReturn(inm); // FIX clave

        Disponibilidad d1 = mock(Disponibilidad.class);
        Disponibilidad d2 = mock(Disponibilidad.class);

        when(gestorUsuarios.esPropietario("maria")).thenReturn(false);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inm);
        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(mock(Inquilino.class));
        when(gestorDisponibilidad.obtenerDisponibilidadParaReserva(eq(10L), any(), any()))
                .thenReturn(List.of(d1, d2));
        when(gestorDisponibilidad.calcularPoliticaCancelacion(any())).thenReturn(PoliticaCancelacion.NO_REEMBOLSABLE);
        when(gestorDisponibilidad.calcularTipoReserva(any())).thenReturn(false);
        when(reserva.getId()).thenReturn(50L);

        mockMvc.perform(post("/reserva/crear/10")
                        .session(session)
                        .flashAttr("reserva", reserva))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pago/confirmarPago/50"));

        verify(gestorReservas).registrarReserva(reserva);
    }

    // -------------------------------------------------------------------------
    // 5. GET /misReservas/{username}
    // -------------------------------------------------------------------------

    @Test
    void testVerMisReservasUsuarioNoValido() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/reserva/misReservas/maria").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testVerMisReservasCorrecto_Inquilino() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Reserva r = mock(Reserva.class);
        when(r.getEstado()).thenReturn(EstadoReserva.ACEPTADA);
        when(r.getPagado()).thenReturn(true);

        when(gestorUsuarios.esPropietario("maria")).thenReturn(false);
        when(gestorReservas.obtenerReservasPorInquilino("maria"))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/reserva/misReservas/maria").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("misReservas"))
                .andExpect(model().attributeExists("reservas"))
                .andExpect(model().attributeExists("esPropietario"));
    }

    @Test
    void testVerMisReservasCorrecto_Propietario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Reserva r = mock(Reserva.class);
        when(r.getEstado()).thenReturn(EstadoReserva.ACEPTADA);
        when(r.getPagado()).thenReturn(true);

        when(gestorUsuarios.esPropietario("juan")).thenReturn(true);
        when(gestorReservas.obtenerReservasPorPropietario("juan"))
                .thenReturn(List.of(r));

        mockMvc.perform(get("/reserva/misReservas/juan").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("misReservas"))
                .andExpect(model().attributeExists("reservas"))
                .andExpect(model().attributeExists("esPropietario"));
    }

    // -------------------------------------------------------------------------
    // 6. POST /cancelar/{idReserva}
    // -------------------------------------------------------------------------

    @Test
    void testCancelarReservaSinSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/reserva/cancelar/99").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testCancelarReserva() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Pago pago = mock(Pago.class);
        SolicitudReserva solicitud = mock(SolicitudReserva.class);

        when(gestorPagos.obtenerPagoPorReserva(99L)).thenReturn(pago);
        when(gestorSolicitudes.obtenerSolicitudporIDreserva(99L)).thenReturn(solicitud);

        mockMvc.perform(post("/reserva/cancelar/99").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserva/misReservas/maria"));

        verify(gestorPagos).borrarPago(pago);
        verify(gestorSolicitudes).borrarSolicitudReserva(solicitud);
        verify(gestorReservas).cancelarReserva(99L);
    }
}
