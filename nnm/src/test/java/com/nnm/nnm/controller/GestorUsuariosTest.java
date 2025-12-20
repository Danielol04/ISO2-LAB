package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;
import com.nnm.nnm.negocio.dominio.entidades.Usuario;
import com.nnm.nnm.persistencia.InquilinoDAO;
import com.nnm.nnm.persistencia.PropietarioDAO;
import com.nnm.nnm.persistencia.UsuarioDAO;

@ExtendWith(MockitoExtension.class)
class GestorUsuariosTest {

    @Mock private UsuarioDAO usuarioDAO;
    @Mock private InquilinoDAO inquilinoDAO;
    @Mock private PropietarioDAO propietarioDAO;

    @InjectMocks private GestorUsuarios gestor;

    // -------------------------------------------------------------------------
    // 1. login
    // -------------------------------------------------------------------------

    @Test
    void testLoginExitoso() {
        Inquilino mockUser = new Inquilino();
        mockUser.setUsername("saul");
        mockUser.setPassword("1234");

        when(usuarioDAO.findByUsername("saul")).thenReturn(mockUser);

        assertTrue(gestor.login("saul", "1234"));
        assertFalse(gestor.login("saul", "incorrecta"));
    }

    @Test
    void testLoginUsuarioNoExiste() {
        when(usuarioDAO.findByUsername("nadie")).thenReturn(null);

        assertFalse(gestor.login("nadie", "1234"));
    }

    // -------------------------------------------------------------------------
    // 2. existeUsuario
    // -------------------------------------------------------------------------

    @Test
    void testExisteUsuarioTrue() {
        Usuario usuario = mock(Usuario.class);
        when(usuarioDAO.findByUsername("pepe")).thenReturn(usuario);

        assertTrue(gestor.existeUsuario("pepe"));
    }

    @Test
    void testExisteUsuarioFalse() {
        when(usuarioDAO.findByUsername("pepe")).thenReturn(null);

        assertFalse(gestor.existeUsuario("pepe"));
    }

    // -------------------------------------------------------------------------
    // 3. registrarInquilino
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarInquilino() {
        Inquilino inq = new Inquilino();

        gestor.registrarInquilino(inq);

        verify(inquilinoDAO).save(inq);
    }

    // -------------------------------------------------------------------------
    // 4. registrarPropietario
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarPropietario() {
        Propietario prop = new Propietario();

        gestor.registrarPropietario(prop);

        verify(propietarioDAO).save(prop);
    }

    // -------------------------------------------------------------------------
    // 5. obtenerPropietarioPorUsername
    // -------------------------------------------------------------------------

    @Test
    void testObtenerPropietarioPorUsername() {
        Propietario prop = new Propietario();
        when(propietarioDAO.findByUsername("prop1")).thenReturn(prop);

        assertEquals(prop, gestor.obtenerPropietarioPorUsername("prop1"));
    }

    // -------------------------------------------------------------------------
    // 6. obtenerInquilinoPorUsername
    // -------------------------------------------------------------------------

    @Test
    void testObtenerInquilinoPorUsername() {
        Inquilino inq = new Inquilino();
        when(inquilinoDAO.findByUsername("inq1")).thenReturn(inq);

        assertEquals(inq, gestor.obtenerInquilinoPorUsername("inq1"));
    }

    // -------------------------------------------------------------------------
    // 7. esPropietario / esInquilino
    // -------------------------------------------------------------------------

    @Test
    void testEsPropietarioOInquilino() {
        Propietario prop = new Propietario();
        when(usuarioDAO.findByUsername("prop1")).thenReturn(prop);

        assertTrue(gestor.esPropietario("prop1"));
        assertFalse(gestor.esInquilino("prop1"));
    }

    @Test
    void testEsInquilino() {
        Inquilino inq = new Inquilino();
        when(usuarioDAO.findByUsername("inq1")).thenReturn(inq);

        assertTrue(gestor.esInquilino("inq1"));
        assertFalse(gestor.esPropietario("inq1"));
    }

    @Test
    void testEsPropietarioOInquilinoUsuarioNoExiste() {
        when(usuarioDAO.findByUsername("nadie")).thenReturn(null);

        assertFalse(gestor.esPropietario("nadie"));
        assertFalse(gestor.esInquilino("nadie"));
    }
}
