package com.nnm.nnm.persistencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

class DisponibilidadDAOTest {

    private DisponibilidadDAO dao;
    private GestorBD gestorBD;

    @BeforeEach
    void setup() {
        dao = new DisponibilidadDAO();
        gestorBD = mock(GestorBD.class);

        // Inyectar gestorBD por reflexión
        try {
            var field = EntidadDAO.class.getDeclaredField("gestorBD");
            field.setAccessible(true);
            field.set(dao, gestorBD);
        } catch (Exception e) {
            fail("No se pudo inyectar gestorBD");
        }
    }

    // -------------------------------------------------------------------------
    // 1. findByInmueble
    // -------------------------------------------------------------------------

    @Test
    void testFindByInmueble() {
        List<Disponibilidad> mockResult = List.of(new Disponibilidad());

        when(gestorBD.selectList(
                "FROM Disponibilidad d WHERE d.inmueble.id = :idInmueble",
                Disponibilidad.class,
                "idInmueble",
                10L
        )).thenReturn(mockResult);

        List<Disponibilidad> result = dao.findByInmueble(10L);

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // 2. encontrarAdyacentes
    // -------------------------------------------------------------------------

    @Test
    void testEncontrarAdyacentes() {
        long id = 5L;
        PoliticaCancelacion politica = PoliticaCancelacion.REEMBOLSABLE;
        boolean directa = true;
        LocalDate inicio = LocalDate.of(2025, 1, 10);
        LocalDate fin = LocalDate.of(2025, 1, 15);

        LocalDate esperadoInicio = inicio.minusDays(1);
        LocalDate esperadoFin = fin.plusDays(1);

        String jpql = "FROM Disponibilidad d WHERE d.inmueble.id = :idInmueble AND d.politicaCancelacion = :politica_cancelacion AND d.reservaDirecta = :reservaDirecta AND (d.fechaInicio = :diaDespuesFin OR d.fechaFin = :diaAntesInicio)";

        List<Disponibilidad> mockResult = List.of(new Disponibilidad());

        when(gestorBD.selectList(
                jpql,
                Disponibilidad.class,
                "idInmueble", id,
                "politica_cancelacion", politica,
                "reservaDirecta", directa,
                "diaAntesInicio", esperadoInicio,
                "diaDespuesFin", esperadoFin
        )).thenReturn(mockResult);

        List<Disponibilidad> result = dao.encontrarAdyacentes(id, politica, directa, inicio, fin);

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // 3. findDisponibilidadparaReserva
    // -------------------------------------------------------------------------

    @Test
    void testFindDisponibilidadParaReserva() {
        long id = 7L;
        LocalDate inicio = LocalDate.of(2025, 2, 1);
        LocalDate fin = LocalDate.of(2025, 2, 5);

        String jpql = "FROM Disponibilidad d " +
                "WHERE d.inmueble.id = :idInmueble " +
                "AND d.fechaInicio <= :reservaInicio " +
                "AND d.fechaFin >= :reservaFin";

        Disponibilidad mockDisponibilidad = new Disponibilidad();

        when(gestorBD.selectSingle(
                jpql,
                Disponibilidad.class,
                "idInmueble", id,
                "reservaInicio", inicio,
                "reservaFin", fin
        )).thenReturn(mockDisponibilidad);

        Disponibilidad result = dao.findDisponibilidadparaReserva(id, inicio, fin);

        assertNotNull(result);
    }
}
