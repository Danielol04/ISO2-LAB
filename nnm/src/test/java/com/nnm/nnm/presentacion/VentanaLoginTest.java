package com.nnm.nnm.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nnm.nnm.negocio.controller.GestorUsuarios;

class VentanaLoginTest {

    private MockMvc mockMvc;

    @Mock
    private GestorUsuarios gestorUsuarios;

    @InjectMocks
    private VentanaLogin controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        var resolver = new org.springframework.web.servlet.view.InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
            .standaloneSetup(controlador)
            .setViewResolvers(resolver)
            .build();
    }


    // -------------------------------------------------------------------------
    // 1. GET /login
    // -------------------------------------------------------------------------

    @Test
    void testMostrarLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /login → login fallido
    // -------------------------------------------------------------------------

    @Test
    void testProcesarLoginFallido() throws Exception {
        when(gestorUsuarios.login("juan", "1234")).thenReturn(false);

        mockMvc.perform(post("/login")
                        .param("username", "juan")
                        .param("password", "1234"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    // -------------------------------------------------------------------------
    // 3. POST /login → login exitoso
    // -------------------------------------------------------------------------

    @Test
    void testProcesarLoginExitoso() throws Exception {
        MockHttpSession session = new MockHttpSession();

        when(gestorUsuarios.login("juan", "1234")).thenReturn(true);

        mockMvc.perform(post("/login")
                        .session(session)
                        .param("username", "juan")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        // Verificar que se guardó en sesión
        assert session.getAttribute("username").equals("juan");
    }

    // -------------------------------------------------------------------------
    // 4. GET /logout
    // -------------------------------------------------------------------------

    @Test
    void testCerrarSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // La sesión debe invalidarse
        assert session.isInvalid();
    }
}
