package com.nnm.nnm.persistencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;

class ReservaDAOTest {

    private ReservaDAO reservaDAO;
    private GestorBD gestorBD;

    @BeforeEach
    void setup() {
        reservaDAO = new ReservaDAO();
        gestorBD = mock(GestorBD.class);

        // Inyectar gestorBD en EntidadDAO mediante reflexión
        try {
            var field = EntidadDAO.class.getDeclaredField("gestorBD");
            field.setAccessible(true);
            field.set(reservaDAO, gestorBD);
        } catch (Exception e) {
            fail("No se pudo inyectar gestorBD en ReservaDAO");
        }
    }

    // -------------------------------------------------------------------------
    // 1. findReservasByInmuebleId
    // -------------------------------------------------------------------------

    @Test
    void testFindReservasByInmuebleId() {
        List<Reserva> mockResult = List.of(new Reserva());

        when(gestorBD.selectList(
                "SELECT r FROM Reserva r WHERE r.inmueble.id = :inmuebleId",
                Reserva.class,
                "inmuebleId",
                10L
        )).thenReturn(mockResult);

        List<Reserva> result = reservaDAO.findReservasByInmuebleId(10L);

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // 2. findReservasByInquilinoUsername
    // -------------------------------------------------------------------------

    @Test
    void testFindReservasByInquilinoUsername() {
        List<Reserva> mockResult = List.of(new Reserva());

        when(gestorBD.selectList(
                "SELECT r FROM Reserva r WHERE r.inquilino.username = :username",
                Reserva.class,
                "username",
                "juan"
        )).thenReturn(mockResult);

        List<Reserva> result = reservaDAO.findReservasByInquilinoUsername("juan");

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // 3. findReservasByPropietarioUsername
    // -------------------------------------------------------------------------

    @Test
    void testFindReservasByPropietarioUsername() {
        List<Reserva> mockResult = List.of(new Reserva());

        when(gestorBD.selectList(
                "SELECT r FROM Reserva r WHERE r.inmueble.propietario.username = :username",
                Reserva.class,
                "username",
                "maria"
        )).thenReturn(mockResult);

        List<Reserva> result = reservaDAO.findReservasByPropietarioUsername("maria");

        assertEquals(1, result.size());
    }
}
