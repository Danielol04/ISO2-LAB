package com.nnm.nnm.presentacion;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

class VentanaSolicitudesTest {

    private MockMvc mockMvc;

    @Mock private GestorSolicitudes gestorSolicitudes;
    @Mock private GestorPagos gestorPagos;
    @Mock private GestorReservas gestorReservas;

    @InjectMocks private VentanaSolicitudes controlador;

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
    // 1. GET /solicitudes/confirmacionReserva/{username}
    // -------------------------------------------------------------------------

    @Test
    void testVerSolicitudesUsuarioNoLogueado() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/solicitudes/confirmacionReserva/juan").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testVerSolicitudesCorrecto() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        SolicitudReserva sol = mock(SolicitudReserva.class);

        when(gestorSolicitudes.obtenerSolicitudesPorPropietario("juan"))
                .thenReturn(List.of(sol));

        mockMvc.perform(get("/solicitudes/confirmacionReserva/juan").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("solicitudes"))
                .andExpect(model().attributeExists("solicitudes"))
                .andExpect(model().attribute("solicitudSeleccionada", (Object) null))
                .andExpect(model().attribute("username", "juan"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /solicitud/{id}/aceptar
    // -------------------------------------------------------------------------

    @Test
    void testAceptarSolicitud() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        mockMvc.perform(post("/solicitudes/solicitud/5/aceptar").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes/confirmacionReserva/juan"));

        verify(gestorSolicitudes).aceptarSolicitudReserva(5L);
    }

    // -------------------------------------------------------------------------
    // 3. POST /solicitud/{id}/rechazar
    // -------------------------------------------------------------------------

    @Test
    void testRechazarSolicitud() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        SolicitudReserva solicitud = mock(SolicitudReserva.class);
        Reserva reserva = mock(Reserva.class);
        Pago pago = mock(Pago.class);

        when(solicitud.getReserva()).thenReturn(reserva);
        when(reserva.getId()).thenReturn(99L);
        when(pago.getId()).thenReturn(123L);

        when(gestorSolicitudes.obtenerSolicitudPorId(5L)).thenReturn(solicitud);
        when(reserva.getPago()).thenReturn(pago);

        mockMvc.perform(post("/solicitudes/solicitud/5/rechazar").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitudes/confirmacionReserva/juan"));

        verify(gestorSolicitudes).borrarSolicitudReserva(solicitud);
        verify(gestorReservas).cancelarReserva(99L);
    }
}
