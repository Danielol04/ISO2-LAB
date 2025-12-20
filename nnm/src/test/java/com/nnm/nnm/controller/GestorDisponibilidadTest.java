package com.nnm.nnm.controller;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.persistencia.DisponibilidadDAO;

@ExtendWith(MockitoExtension.class)
class GestorDisponibilidadTest {

    @Mock
    private DisponibilidadDAO disponibilidadDAO;

    @InjectMocks
    private GestorDisponibilidad gestor;

    // -------------------------------------------------------------------------
    // 1. calcularPoliticaCancelacion
    // -------------------------------------------------------------------------

    @Test
    void testCalcularPoliticaMasRestrictiva() {
        GestorDisponibilidad g = new GestorDisponibilidad(null);

        Disponibilidad d1 = new Disponibilidad();
        d1.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);

        Disponibilidad d2 = new Disponibilidad();
        d2.setPoliticaCancelacion(PoliticaCancelacion.NO_REEMBOLSABLE);

        PoliticaCancelacion resultado = g.calcularPoliticaCancelacion(List.of(d1, d2));

        assertEquals(PoliticaCancelacion.NO_REEMBOLSABLE, resultado);
    }

    @Test
    void testCalcularPoliticaConListaVacia() {
        GestorDisponibilidad g = new GestorDisponibilidad(null);
        PoliticaCancelacion resultado = g.calcularPoliticaCancelacion(List.of());
        assertEquals(PoliticaCancelacion.REEMBOLSABLE, resultado);
    }

    @Test
    void testCalcularPoliticaTodasIguales() {
        GestorDisponibilidad g = new GestorDisponibilidad(null);

        Disponibilidad d1 = new Disponibilidad();
        d1.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE_50_PER);

        Disponibilidad d2 = new Disponibilidad();
        d2.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE_50_PER);

        PoliticaCancelacion resultado = g.calcularPoliticaCancelacion(List.of(d1, d2));

        assertEquals(PoliticaCancelacion.REEMBOLSABLE_50_PER, resultado);
    }

    // -------------------------------------------------------------------------
    // 2. calcularTipoReserva
    // -------------------------------------------------------------------------

    @Test
    void testCalcularTipoReservaTodasDirectas() {
        GestorDisponibilidad g = new GestorDisponibilidad(null);

        Disponibilidad d1 = new Disponibilidad();
        d1.setReservaDirecta(true);

        Disponibilidad d2 = new Disponibilidad();
        d2.setReservaDirecta(true);

        assertTrue(g.calcularTipoReserva(List.of(d1, d2)));
    }

    @Test
    void testCalcularTipoReservaUnaNoDirecta() {
        GestorDisponibilidad g = new GestorDisponibilidad(null);

        Disponibilidad d1 = new Disponibilidad();
        d1.setReservaDirecta(true);

        Disponibilidad d2 = new Disponibilidad();
        d2.setReservaDirecta(false);

        assertFalse(g.calcularTipoReserva(List.of(d1, d2)));
    }

    // -------------------------------------------------------------------------
    // 3. obtenerDisponibilidadParaReserva
    // -------------------------------------------------------------------------

    class GestorDisponibilidadFake extends GestorDisponibilidad {
        private List<Disponibilidad> datos;

        public GestorDisponibilidadFake(List<Disponibilidad> datos) {
            super(null);
            this.datos = datos;
        }

        @Override
        public List<Disponibilidad> obtenerDisponibilidadPorInmueble(long id) {
            return datos;
        }
    }

    @Test
    void testObtenerDisponibilidadParaReservaSolapa() {
        Disponibilidad d = new Disponibilidad();
        d.setFechaInicio(LocalDate.of(2025, 1, 1));
        d.setFechaFin(LocalDate.of(2025, 1, 10));

        GestorDisponibilidadFake g = new GestorDisponibilidadFake(List.of(d));

        List<Disponibilidad> res = g.obtenerDisponibilidadParaReserva(
                1,
                LocalDate.of(2025, 1, 5),
                LocalDate.of(2025, 1, 7)
        );

        assertEquals(1, res.size());
    }

    @Test
    void testObtenerDisponibilidadParaReservaNoSolapa() {
        Disponibilidad d = new Disponibilidad();
        d.setFechaInicio(LocalDate.of(2025, 1, 1));
        d.setFechaFin(LocalDate.of(2025, 1, 10));

        GestorDisponibilidadFake g = new GestorDisponibilidadFake(List.of(d));

        List<Disponibilidad> res = g.obtenerDisponibilidadParaReserva(
                1,
                LocalDate.of(2025, 1, 11),
                LocalDate.of(2025, 1, 15)
        );

        assertTrue(res.isEmpty());
    }

    // -------------------------------------------------------------------------
    // 4. registrarDisponibilidad (con Mockito + Inmueble asignado)
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarDisponibilidadSinAdyacentes() {
        Inmueble inmueble = mock(Inmueble.class);
        when(inmueble.getId()).thenReturn(1L);

        Disponibilidad nueva = new Disponibilidad();
        nueva.setInmueble(inmueble);
        nueva.setFechaInicio(LocalDate.of(2025, 1, 1));
        nueva.setFechaFin(LocalDate.of(2025, 1, 5));
        nueva.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);
        nueva.setReservaDirecta(true);

        when(disponibilidadDAO.encontrarAdyacentes(
                eq(1L), eq(PoliticaCancelacion.REEMBOLSABLE), eq(true),
                eq(LocalDate.of(2025, 1, 1)), eq(LocalDate.of(2025, 1, 5))
        )).thenReturn(List.of());

        gestor.registrarDisponibilidad(nueva);

        verify(disponibilidadDAO).save(nueva);
    }


    @Test
    void testRegistrarDisponibilidadConAdyacentes() {
        Inmueble inmueble = mock(Inmueble.class);
        when(inmueble.getId()).thenReturn(1L);

        Disponibilidad nueva = new Disponibilidad();
        nueva.setInmueble(inmueble);
        nueva.setFechaInicio(LocalDate.of(2025, 1, 5));
        nueva.setFechaFin(LocalDate.of(2025, 1, 10));
        nueva.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);
        nueva.setReservaDirecta(true);

        Disponibilidad d1 = new Disponibilidad();
        d1.setInmueble(inmueble);
        d1.setFechaInicio(LocalDate.of(2025, 1, 1));
        d1.setFechaFin(LocalDate.of(2025, 1, 4));
        d1.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);
        d1.setReservaDirecta(true);

        when(disponibilidadDAO.encontrarAdyacentes(
                eq(1L), eq(PoliticaCancelacion.REEMBOLSABLE), eq(true),
                eq(LocalDate.of(2025, 1, 5)), eq(LocalDate.of(2025, 1, 10))
        )).thenReturn(List.of(d1));

        gestor.registrarDisponibilidad(nueva);

        verify(disponibilidadDAO).delete(d1);

        verify(disponibilidadDAO).save(argThat(fusionada ->
                fusionada.getFechaInicio().equals(LocalDate.of(2025, 1, 1)) &&
                fusionada.getFechaFin().equals(LocalDate.of(2025, 1, 10))
        ));
    }

}
