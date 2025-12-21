package com.nnm.nnm.persistencia;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

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