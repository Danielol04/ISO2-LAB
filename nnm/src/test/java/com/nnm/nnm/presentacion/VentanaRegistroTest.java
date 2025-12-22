package com.nnm.nnm.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.nnm.nnm.negocio.controller.GestorUsuarios;

class VentanaRegistroTest {

    private MockMvc mockMvc;

    @Mock
    private GestorUsuarios gestorUsuarios;

    @InjectMocks
    private VentanaRegistro controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // NECESARIO para evitar circular view path con "registro" y "login"
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setViewResolvers(resolver)
                .build();
    }

    // -------------------------------------------------------------------------
    // 1. GET /registro
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormulario() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /registro → usuario ya existe
    // -------------------------------------------------------------------------

    @Test
    void testProcesarRegistroUsuarioYaExiste() throws Exception {
        when(gestorUsuarios.existeUsuario("juan")).thenReturn(true);

        mockMvc.perform(post("/registro")
                        .param("userType", "PROPIETARIO")
                        .param("username", "juan")
                        .param("password", "1234")
                        .param("correo", "a@a.com")
                        .param("nombre", "Juan")
                        .param("apellidos", "Pérez")
                        .param("direccion", "Calle 1"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("error"));
    }

    // -------------------------------------------------------------------------
    // 3. POST /registro → registro propietario
    // -------------------------------------------------------------------------

    @Test
    void testProcesarRegistroPropietario() throws Exception {
        when(gestorUsuarios.existeUsuario("juan")).thenReturn(false);

        mockMvc.perform(post("/registro")
                        .param("userType", "PROPIETARIO")
                        .param("username", "juan")
                        .param("password", "1234")
                        .param("correo", "a@a.com")
                        .param("nombre", "Juan")
                        .param("apellidos", "Pérez")
                        .param("direccion", "Calle 1"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("mensaje"));

        verify(gestorUsuarios).registrarPropietario(any());
    }

    // -------------------------------------------------------------------------
    // 4. POST /registro → registro inquilino
    // -------------------------------------------------------------------------

    @Test
    void testProcesarRegistroInquilino() throws Exception {
        when(gestorUsuarios.existeUsuario("maria")).thenReturn(false);

        mockMvc.perform(post("/registro")
                        .param("userType", "INQUILINO")
                        .param("username", "maria")
                        .param("password", "abcd")
                        .param("correo", "b@b.com")
                        .param("nombre", "Maria")
                        .param("apellidos", "López")
                        .param("direccion", "Calle 2"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("mensaje"));

        verify(gestorUsuarios).registrarInquilino(any());
    }

    // -------------------------------------------------------------------------
    // 5. POST /registro → tipo de usuario inválido
    // -------------------------------------------------------------------------

    @Test
    void testProcesarRegistroTipoInvalido() throws Exception {
        when(gestorUsuarios.existeUsuario("x")).thenReturn(false);

        mockMvc.perform(post("/registro")
                        .param("userType", "ADMIN")
                        .param("username", "x")
                        .param("password", "1234")
                        .param("correo", "c@c.com")
                        .param("nombre", "X")
                        .param("apellidos", "Y")
                        .param("direccion", "Z"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("error"));
    }
}
