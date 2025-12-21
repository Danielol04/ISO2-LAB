package com.nnm.nnm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.persistencia.PagoDAO;

@ExtendWith(MockitoExtension.class)
class GestorPagosTest {

    @Mock
    private PagoDAO pagoDAO;

    @InjectMocks
    private GestorPagos gestorPagos;

    @Test
    void testRegistrarPago() {
        Pago pago = new Pago();

        gestorPagos.registrarPago(pago);

        verify(pagoDAO).guardarPago(pago);
    }
}
