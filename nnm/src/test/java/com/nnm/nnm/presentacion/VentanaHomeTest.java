package com.nnm.nnm.presentacion;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

class VentanaHomeTest {

    private MockMvc mockMvc;

    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorBusquedas gestorBusquedas;
    @Mock private GestorUsuarios gestorUsuarios;

    @InjectMocks private VentanaHome controlador;

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
    // 1. GET /home → propietario
    // -------------------------------------------------------------------------

    @Test
    void testHomePropietario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorUsuarios.esPropietario("juan")).thenReturn(true);
        when(gestorInmuebles.listarInmueblesPorPropietario("juan"))
                .thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homePropietario"))
                .andExpect(model().attributeExists("propiedades"))
                .andExpect(model().attribute("username", "juan"));
    }

    // -------------------------------------------------------------------------
    // 2. GET /home → inquilino
    // -------------------------------------------------------------------------

    @Test
    void testHomeInquilino() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        when(gestorUsuarios.esPropietario("maria")).thenReturn(false);
        when(gestorUsuarios.esInquilino("maria")).thenReturn(true);
        when(gestorInmuebles.listarInmuebles()).thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homeInquilino"))
                .andExpect(model().attributeExists("propiedades"))
                .andExpect(model().attribute("username", "maria"));
    }

    // -------------------------------------------------------------------------
    // 3. GET /home → público (sin rol)
    // -------------------------------------------------------------------------

    @Test
    void testHomePublico() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "anon");

        when(gestorUsuarios.esPropietario("anon")).thenReturn(false);
        when(gestorUsuarios.esInquilino("anon")).thenReturn(false);
        when(gestorInmuebles.listarInmuebles()).thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/home").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("propiedades"));
    }

    // -------------------------------------------------------------------------
    // 4. GET /buscar → propietario
    // -------------------------------------------------------------------------

    @Test
    void testBuscarPropietario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorUsuarios.esPropietario("juan")).thenReturn(true);
        when(gestorBusquedas.buscar(any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/buscar")
                        .param("destino", "Madrid")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homePropietario"))
                .andExpect(model().attributeExists("propiedades"));
    }

    // -------------------------------------------------------------------------
    // 5. GET /buscar → inquilino
    // -------------------------------------------------------------------------

    @Test
    void testBuscarInquilino() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        when(gestorUsuarios.esPropietario("maria")).thenReturn(false);
        when(gestorUsuarios.esInquilino("maria")).thenReturn(true);
        when(gestorBusquedas.buscar(any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/buscar")
                        .param("destino", "Madrid")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("homeInquilino"))
                .andExpect(model().attributeExists("propiedades"));
    }

    // -------------------------------------------------------------------------
    // 6. GET /buscar → público
    // -------------------------------------------------------------------------

    @Test
    void testBuscarPublico() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "anon");

        when(gestorUsuarios.esPropietario("anon")).thenReturn(false);
        when(gestorUsuarios.esInquilino("anon")).thenReturn(false);
        when(gestorBusquedas.buscar(any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Inmueble()));

        mockMvc.perform(get("/buscar")
                        .param("destino", "Madrid")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("propiedades"));
    }
}
