package com.nnm.nnm.presentacion;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nnm.nnm.negocio.controller.GestorListaDeseos;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;

class VentanaListaDeseosTest {

    private MockMvc mockMvc;

    @Mock private GestorListaDeseos gestorLista;
    @Mock private GestorUsuarios gestorUsuarios;

    @InjectMocks private VentanaListaDeseos controlador;

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
    // 1. POST /favoritos/toggle → usuario logueado
    // -------------------------------------------------------------------------

    @Test
    void testToggleFavoritoLogueado() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        when(gestorLista.toggleInmueble(10L, "maria")).thenReturn(true);

        mockMvc.perform(post("/favoritos/toggle")
                        .param("idInmueble", "10")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /favoritos/toggle → no logueado
    // -------------------------------------------------------------------------

    @Test
    void testToggleFavoritoNoLogueado() throws Exception {
        mockMvc.perform(post("/favoritos/toggle")
                        .param("idInmueble", "10"))
                .andExpect(status().isUnauthorized());
    }

    // -------------------------------------------------------------------------
    // 3. GET /favoritos/lista → logueado con lista
    // -------------------------------------------------------------------------

    @Test
    void testObtenerFavoritosConLista() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Inmueble inm = mock(Inmueble.class);
        when(inm.getId()).thenReturn(5L);

        ListaDeseos lista = new ListaDeseos();
        lista.setInmuebles(Set.of(inm));

        Inquilino inq = new Inquilino();
        inq.setListaDeseos(lista);

        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(inq);

        mockMvc.perform(get("/favoritos/lista").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json("[5]"));
    }

    // -------------------------------------------------------------------------
    // 4. GET /favoritos/lista → logueado sin lista
    // -------------------------------------------------------------------------

    @Test
    void testObtenerFavoritosSinLista() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Inquilino inq = new Inquilino();
        inq.setListaDeseos(null);

        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(inq);

        mockMvc.perform(get("/favoritos/lista").session(session))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // -------------------------------------------------------------------------
    // 5. GET /favoritos/lista → no logueado
    // -------------------------------------------------------------------------

    @Test
    void testObtenerFavoritosNoLogueado() throws Exception {
        mockMvc.perform(get("/favoritos/lista"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // -------------------------------------------------------------------------
    // 6. GET /lista-deseos → logueado con lista
    // -------------------------------------------------------------------------

    @Test
    void testMostrarListaDeseosConLista() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Inmueble inm = new Inmueble();
        ListaDeseos lista = new ListaDeseos();
        lista.setInmuebles(Set.of(inm));

        Inquilino inq = new Inquilino();
        inq.setListaDeseos(lista);

        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(inq);

        mockMvc.perform(get("/lista-deseos").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("propiedades"))
                .andExpect(view().name("lista-deseos"));
    }

    // -------------------------------------------------------------------------
    // 7. GET /lista-deseos → logueado sin lista
    // -------------------------------------------------------------------------

    @Test
    void testMostrarListaDeseosSinLista() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        Inquilino inq = new Inquilino();
        inq.setListaDeseos(null);

        when(gestorUsuarios.obtenerInquilinoPorUsername("maria")).thenReturn(inq);

        mockMvc.perform(get("/lista-deseos").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("propiedades"))
                .andExpect(view().name("lista-deseos"));
    }

    // -------------------------------------------------------------------------
    // 8. GET /lista-deseos → no logueado
    // -------------------------------------------------------------------------

    @Test
    void testMostrarListaDeseosNoLogueado() throws Exception {
        mockMvc.perform(get("/lista-deseos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
