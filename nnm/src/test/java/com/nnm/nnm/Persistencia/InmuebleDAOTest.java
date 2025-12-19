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
import com.nnm.nnm.persistencia.InmuebleDAO;

@ExtendWith(MockitoExtension.class)
class InmuebleDAOTest {

    @Mock
    private GestorBD gestorBD;

    @InjectMocks
    private InmuebleDAO inmuebleDAO;

    @Test
    void testFindByPropietario() {
        String username = "propietarioTest";
        List<Inmueble> inmueblesFake = List.of(new Inmueble());
        
        when(gestorBD.selectList(anyString(), eq(Inmueble.class), eq("username"), eq(username)))
            .thenReturn(inmueblesFake);

        List<Inmueble> resultado = inmuebleDAO.findByPropietario(username);

        assertEquals(1, resultado.size());
        verify(gestorBD).selectList(contains("username = :username"), eq(Inmueble.class), eq("username"), eq(username));
    }

    @Test
    void testSave() {
        Inmueble i = new Inmueble();
        inmuebleDAO.save(i);
        verify(gestorBD).insert(i);
    }
}