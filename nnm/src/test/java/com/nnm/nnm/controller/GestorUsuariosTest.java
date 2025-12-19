package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;
import com.nnm.nnm.persistencia.InquilinoDAO;
import com.nnm.nnm.persistencia.PropietarioDAO;
import com.nnm.nnm.persistencia.UsuarioDAO;

@ExtendWith(MockitoExtension.class)
class GestorUsuariosTest {

    @Mock private UsuarioDAO usuarioDAO;
    @Mock private InquilinoDAO inquilinoDAO;
    @Mock private PropietarioDAO propietarioDAO;

    @InjectMocks private GestorUsuarios gestor;

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
    void testEsPropietarioOInquilino() {
        Propietario prop = new Propietario();
        when(usuarioDAO.findByUsername("prop1")).thenReturn(prop);
        
        assertTrue(gestor.esPropietario("prop1"));
        assertFalse(gestor.esInquilino("prop1"));
    }
}