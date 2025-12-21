package com.nnm.nnm.persistencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Pago;

class PagoDAOTest {

    private PagoDAO pagoDAO;
    private GestorBD gestorBD;

    @BeforeEach
    void setup() {
        pagoDAO = new PagoDAO();
        gestorBD = mock(GestorBD.class);

        // Inyectar gestorBD en EntidadDAO mediante reflexión
        try {
            var field = EntidadDAO.class.getDeclaredField("gestorBD");
            field.setAccessible(true);
            field.set(pagoDAO, gestorBD);
        } catch (Exception e) {
            fail("No se pudo inyectar gestorBD en PagoDAO");
        }
    }

    // -------------------------------------------------------------------------
    // 1. guardarPago()
    // -------------------------------------------------------------------------

    @Test
    void testGuardarPago() {
        Pago pago = new Pago();

        pagoDAO.guardarPago(pago);

        verify(gestorBD).insert(pago);
    }

    // -------------------------------------------------------------------------
    // 2. findByReservaID() — resultado encontrado
    // -------------------------------------------------------------------------

    @Test
    void testFindByReservaID_OK() {
        Pago pago = new Pago();

        when(gestorBD.selectSingle(
                "SELECT p FROM Pago p WHERE p.idReserva = :idReserva",
                Pago.class,
                "idReserva",
                10L
        )).thenReturn(pago);

        Pago result = pagoDAO.findByReservaID(10L);

        assertNotNull(result);
        assertEquals(pago, result);
    }

    // -------------------------------------------------------------------------
    // 3. findByReservaID() — sin resultado (null)
    // -------------------------------------------------------------------------

    @Test
    void testFindByReservaID_NoResult() {
        when(gestorBD.selectSingle(
                "SELECT p FROM Pago p WHERE p.idReserva = :idReserva",
                Pago.class,
                "idReserva",
                10L
        )).thenReturn(null);

        Pago result = pagoDAO.findByReservaID(10L);

        assertNull(result);
    }
}
