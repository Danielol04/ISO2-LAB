package com.nnm.nnm.persistencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

class GestorBDTest {

    private GestorBD gestorBD;
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        gestorBD = new GestorBD();
        entityManager = mock(EntityManager.class);

        // Inyectar entityManager por reflexión
        try {
            var field = GestorBD.class.getDeclaredField("entityManager");
            field.setAccessible(true);
            field.set(gestorBD, entityManager);
        } catch (Exception e) {
            fail("No se pudo inyectar entityManager");
        }
    }

    // -------------------------------------------------------------------------
    // select
    // -------------------------------------------------------------------------

    @Test
    void testSelectOK() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of("A", "B"));

        List<String> result = gestorBD.select("JPQL", String.class);

        assertEquals(2, result.size());
    }

    @Test
    void testSelectNoResult() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        List<String> result = gestorBD.select("JPQL", String.class);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // selectSingle
    // -------------------------------------------------------------------------

    @Test
    void testSelectSingleOK() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);
        when(query.setParameter("id", 10)).thenReturn(query);
        when(query.getSingleResult()).thenReturn("OK");

        String result = gestorBD.selectSingle("JPQL", String.class, "id", 10);

        assertEquals("OK", result);
    }

    @Test
    void testSelectSingleWithExtras() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);

        when(query.setParameter("id", 10)).thenReturn(query);
        when(query.setParameter("extra1", "A")).thenReturn(query);
        when(query.setParameter("extra2", "B")).thenReturn(query);

        when(query.getSingleResult()).thenReturn("OK");

        String result = gestorBD.selectSingle(
                "JPQL",
                String.class,
                "id",
                10,
                "extra1", "A",
                "extra2", "B"
        );

        assertEquals("OK", result);
    }

    @Test
    void testSelectSingleNoResult() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);
        when(query.setParameter("id", 10)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException());

        String result = gestorBD.selectSingle("JPQL", String.class, "id", 10);

        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // selectList
    // -------------------------------------------------------------------------

    @Test
    void testSelectListOK() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);

        when(query.setParameter("id", 10)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of("X"));

        List<String> result = gestorBD.selectList("JPQL", String.class, "id", 10);

        assertEquals(1, result.size());
    }

    @Test
    void testSelectListNoResult() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);
        when(query.setParameter("id", 10)).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        List<String> result = gestorBD.selectList("JPQL", String.class, "id", 10);

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // insert
    // -------------------------------------------------------------------------

    @Test
    void testInsert() {
        String entity = "X";

        gestorBD.insert(entity);

        verify(entityManager).persist(entity);
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    void testUpdateOK() {
        String entity = "X";
        when(entityManager.merge(entity)).thenReturn("UPDATED");

        String result = gestorBD.update(entity);

        assertEquals("UPDATED", result);
    }

    @Test
    void testUpdateException() {
        String entity = "X";
        when(entityManager.merge(entity)).thenThrow(new RuntimeException());

        String result = gestorBD.update(entity);

        assertNull(result);
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    void testDeleteEntityManaged() {
        String entity = "X";

        when(entityManager.contains(entity)).thenReturn(true);

        gestorBD.delete(entity);

        verify(entityManager).remove(entity);
    }

    @Test
    void testDeleteEntityNotManaged() {
        String entity = "X";

        when(entityManager.contains(entity)).thenReturn(false);
        when(entityManager.merge(entity)).thenReturn("MERGED");

        gestorBD.delete(entity);

        verify(entityManager).remove("MERGED");
    }

    // -------------------------------------------------------------------------
    // selectListConParametros
    // -------------------------------------------------------------------------

    @Test
    void testSelectListConParametrosOK() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);

        when(query.setParameter("p1", 1)).thenReturn(query);
        when(query.setParameter("p2", 2)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of("A"));

        List<String> result = gestorBD.selectListConParametros(
                "JPQL",
                String.class,
                new String[]{"p1", "p2"},
                new Object[]{1, 2}
        );

        assertEquals(1, result.size());
    }

    @Test
    void testSelectListConParametrosNoResult() {
        TypedQuery<String> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", String.class)).thenReturn(query);

        when(query.setParameter("p1", 1)).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        List<String> result = gestorBD.selectListConParametros(
                "JPQL",
                String.class,
                new String[]{"p1"},
                new Object[]{1}
        );

        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // buscarFiltradoInmuebles
    // -------------------------------------------------------------------------

    @Test
    void testBuscarFiltradoInmuebles() {
        TypedQuery<Inmueble> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Inmueble.class))).thenReturn(query);

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Inmueble()));

        List<Inmueble> result = gestorBD.buscarFiltradoInmuebles(
                "madrid",
                2,
                1,
                50.0,
                200.0
        );

        assertEquals(1, result.size());
    }

    // -------------------------------------------------------------------------
    // selectList con múltiples parámetros
    // -------------------------------------------------------------------------

    @Test
    void testSelectListMultipleParamsOK() {
        TypedQuery<Disponibilidad> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", Disponibilidad.class)).thenReturn(query);

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Disponibilidad()));

        List<Disponibilidad> result = gestorBD.selectList(
                "JPQL",
                Disponibilidad.class,
                "idInmueble", 1L,
                "politica", PoliticaCancelacion.REEMBOLSABLE,
                "directa", true,
                "inicio", LocalDate.now(),
                "fin", LocalDate.now().plusDays(1)
        );

        assertEquals(1, result.size());
    }

    @Test
    void testSelectListMultipleParamsNoResult() {
        TypedQuery<Disponibilidad> query = mock(TypedQuery.class);
        when(entityManager.createQuery("JPQL", Disponibilidad.class)).thenReturn(query);

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenThrow(new NoResultException());

        List<Disponibilidad> result = gestorBD.selectList(
                "JPQL",
                Disponibilidad.class,
                "idInmueble", 1L,
                "politica", PoliticaCancelacion.REEMBOLSABLE,
                "directa", true,
                "inicio", LocalDate.now(),
                "fin", LocalDate.now().plusDays(1)
        );

        assertTrue(result.isEmpty());
    }
}
