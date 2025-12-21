package com.nnm.nnm.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.MetodoPago;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class VentanaPagoTest {

    private MockMvc mockMvc;

    @Mock private GestorReservas gestorReservas;
    @Mock private GestorPagos gestorPagos;
    @Mock private GestorSolicitudes gestorSolicitudes;
    @Mock private GestorInmuebles gestorInmuebles;

    @InjectMocks private VentanaPago controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // NECESARIO para evitar circular view path
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setViewResolvers(resolver)
                .build();
    }

    // -------------------------------------------------------------------------
    // 1. GET /confirmarPago/{idReserva}
    // -------------------------------------------------------------------------

    @Test
    void testMostrarPagoReservaNoExiste() throws Exception {
        when(gestorReservas.obtenerReservaPorId(1L)).thenReturn(null);

        mockMvc.perform(get("/pago/confirmarPago/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testMostrarPagoReservaYaPagada() throws Exception {
        Reserva reserva = mock(Reserva.class);
        when(reserva.getPagado()).thenReturn(true);
        when(gestorReservas.obtenerReservaPorId(1L)).thenReturn(reserva);

        mockMvc.perform(get("/pago/confirmarPago/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testMostrarPagoCorrecto() throws Exception {
        Reserva reserva = mock(Reserva.class);
        when(reserva.getPagado()).thenReturn(false);
        when(gestorReservas.obtenerReservaPorId(1L)).thenReturn(reserva);

        mockMvc.perform(get("/pago/confirmarPago/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("pago"))
                .andExpect(model().attributeExists("reserva"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /confirmarPago/{idReserva}
    // -------------------------------------------------------------------------

    @Test
    void testConfirmarPagoReservaDirecta() throws Exception {
        Reserva reserva = mock(Reserva.class);

        Inmueble inm = mock(Inmueble.class);
        when(inm.getId()).thenReturn(10L);

        when(reserva.getInmueble()).thenReturn(inm);
        when(reserva.getReservaDirecta()).thenReturn(true);
        when(gestorReservas.obtenerReservaPorId(1L)).thenReturn(reserva);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inm);

        mockMvc.perform(post("/pago/confirmarPago/1")
                        .param("precioTotal", "100.0")
                        .param("metodoPago", MetodoPago.TARJETA_CREDITO.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserva/crear/10"));

        verify(reserva).setEstado(EstadoReserva.ACEPTADA);
        verify(gestorReservas).actualizarReserva(reserva);
        verify(gestorPagos).registrarPago(any(Pago.class));
    }

    @Test
    void testConfirmarPagoSolicitudReserva() throws Exception {
        Reserva reserva = mock(Reserva.class);

        Inmueble inm = mock(Inmueble.class);
        when(inm.getId()).thenReturn(10L);

        when(reserva.getInmueble()).thenReturn(inm);
        when(reserva.getReservaDirecta()).thenReturn(false);
        when(gestorReservas.obtenerReservaPorId(1L)).thenReturn(reserva);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inm);

        mockMvc.perform(post("/pago/confirmarPago/1")
                        .param("precioTotal", "150.0")
                        .param("metodoPago", MetodoPago.PAYPAL.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserva/crear/10"));

        verify(reserva).setEstado(EstadoReserva.PAGADA);
        verify(gestorSolicitudes).generarSolicitudReserva(reserva, 150.0);
        verify(gestorReservas).actualizarReserva(reserva);
        verify(gestorPagos).registrarPago(any(Pago.class));
    }
}
