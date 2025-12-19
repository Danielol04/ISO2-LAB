package com.nnm.nnm.controller;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.persistencia.PagoDAO;

@ExtendWith(MockitoExtension.class)
class GestorPagosTest {
    @Mock private PagoDAO pagoDAO;
    @InjectMocks private GestorPagos gestor;

    @Test
    void testRegistrarPago() {
        Pago p = new Pago();
        gestor.registrarPago(p);
        verify(pagoDAO).guardarPago(p);
    }
}