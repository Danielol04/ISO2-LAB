package com.nnm.nnm.Persistencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.GestorBD;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class GestorBDTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Inmueble> typedQuery;

    @InjectMocks
    private GestorBD gestorBD;

    @Test
    void testBuscarFiltradoInmueblesConstruccionConsulta() {
        // Simulamos el comportamiento del EntityManager y la Query
        when(entityManager.createQuery(anyString(), eq(Inmueble.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(new Inmueble()));

        // Ejecutamos la búsqueda con varios filtros
        List<Inmueble> resultados = gestorBD.buscarFiltradoInmuebles("Madrid", 2, 1, 50.0, 200.0);

        // Verificamos que se llamó a createQuery y setParameter para cada filtro
        assertNotNull(resultados);
        verify(entityManager).createQuery(contains("LOWER(i.localidad)"), eq(Inmueble.class));
        verify(typedQuery).setParameter(eq("destino"), eq("%madrid%"));
        verify(typedQuery).setParameter(eq("habitaciones"), eq(2));
        verify(typedQuery).setParameter(eq("precioMax"), eq(200.0));
    }

    @Test
    void testInsert() {
        Inmueble i = new Inmueble();
        gestorBD.insert(i);
        verify(entityManager).persist(i);
    }
}