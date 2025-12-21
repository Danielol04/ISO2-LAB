package com.nnm.nnm.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
        mockMvc = MockMvcBuilders.standaloneSetup(controlador).build();
    }

    // -------------------------------------------------------------------------
    // 1. GET /confirmarPago/{idReserva}
    // -------------------------------------------------------------------------

    @Test
    void testMostrarPagoCorrecto() throws Exception {
        Reserva reserva = mock(Reserva.class);
        when(reserva.getPagado()).thenReturn(false);
        when(gestorReservas.obtenerReservaPorId(10L)).thenReturn(reserva);

        mockMvc.perform(get("/pago/confirmarPago/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("pago"))
                .andExpect(model().attributeExists("reserva"));
    }

    @Test
    void testMostrarPagoReservaInvalida() throws Exception {
        when(gestorReservas.obtenerReservaPorId(10L)).thenReturn(null);

        mockMvc.perform(get("/pago/confirmarPago/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testMostrarPagoYaPagado() throws Exception {
        Reserva reserva = mock(Reserva.class);
        when(reserva.getPagado()).thenReturn(true);
        when(gestorReservas.obtenerReservaPorId(10L)).thenReturn(reserva);

        mockMvc.perform(get("/pago/confirmarPago/10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /confirmarPago/{idReserva} — Reserva directa
    // -------------------------------------------------------------------------

    @Test
    void testConfirmarPagoReservaDirecta() throws Exception {
        Reserva reserva = mock(Reserva.class);
        Inmueble inmueble = mock(Inmueble.class);

        when(reserva.getReservaDirecta()).thenReturn(true);
        when(reserva.getInmueble()).thenReturn(inmueble);
        when(inmueble.getId()).thenReturn(10L);

        when(gestorReservas.obtenerReservaPorId(10L)).thenReturn(reserva);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inmueble);

        mockMvc.perform(post("/pago/confirmarPago/10")
                        .param("precioTotal", "200.0")
                        .param("metodoPago", MetodoPago.TARJETA_CREDITO.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserva/crear/10?reservado=true"));

        verify(reserva).setEstado(EstadoReserva.ACEPTADA);
        verify(reserva).setPagado(true);
        verify(gestorReservas).actualizarReserva(reserva);
        verify(gestorPagos).registrarPago(any(Pago.class));
        verify(gestorSolicitudes, never()).generarSolicitudReserva(any(), anyDouble());
    }

    // -------------------------------------------------------------------------
    // 3. POST /confirmarPago/{idReserva} — Solicitud de reserva
    // -------------------------------------------------------------------------

    @Test
    void testConfirmarPagoSolicitudReserva() throws Exception {
        Reserva reserva = mock(Reserva.class);
        Inmueble inmueble = mock(Inmueble.class);

        when(reserva.getReservaDirecta()).thenReturn(false);
        when(reserva.getInmueble()).thenReturn(inmueble);
        when(inmueble.getId()).thenReturn(10L);

        when(gestorReservas.obtenerReservaPorId(10L)).thenReturn(reserva);
        when(gestorInmuebles.obtenerInmueblePorId(10L)).thenReturn(inmueble);

        mockMvc.perform(post("/pago/confirmarPago/10")
                        .param("precioTotal", "200.0")
                        .param("metodoPago", MetodoPago.PAYPAL.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reserva/crear/10?reservado=true"));

        verify(reserva).setEstado(EstadoReserva.PAGADA);
        verify(gestorSolicitudes).generarSolicitudReserva(reserva, 200.0);
        verify(reserva).setPagado(true);
        verify(gestorReservas).actualizarReserva(reserva);
        verify(gestorPagos).registrarPago(any(Pago.class));
    }
}
