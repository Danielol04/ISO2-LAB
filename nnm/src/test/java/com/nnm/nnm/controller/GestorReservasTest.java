package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;

@ExtendWith(MockitoExtension.class)
class GestorReservasTest {
    @Mock private ReservaDAO reservaDAO;
    @InjectMocks private GestorReservas gestor;

    @Test
    void testAutenticarReserva() {
        Inquilino inq = new Inquilino();
        inq.setUsername("juanito");
        Reserva r = new Reserva();
        r.setInquilino(inq);

        when(reservaDAO.findById(1L)).thenReturn(r);
        assertTrue(gestor.autenticarReserva(1L, "juanito"));
        assertFalse(gestor.autenticarReserva(1L, "otro"));
    }
}