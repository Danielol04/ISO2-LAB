package com.nnm.nnm.presentacion;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

class VentanaInmuebleTest {

    private MockMvc mockMvc;

    @Mock private GestorInmuebles gestorInmuebles;
    @Mock private GestorUsuarios gestorUsuarios;

    @InjectMocks private VentanaInmueble controlador;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controlador).build();
    }

    // -------------------------------------------------------------------------
    // 1. GET /alta
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFormulario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        mockMvc.perform(get("/inmuebles/alta").session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("inmueble"))
                .andExpect(model().attribute("username", "juan"))
                .andExpect(view().name("AltaInmuebles"));
    }

    // -------------------------------------------------------------------------
    // 2. POST /alta (registro exitoso)
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarInmuebleExitoso() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        Propietario propietario = new Propietario();
        propietario.setUsername("juan");

        when(gestorUsuarios.esPropietario("juan")).thenReturn(true);
        when(gestorUsuarios.obtenerPropietarioPorUsername("juan")).thenReturn(propietario);

        MockMultipartFile foto = new MockMultipartFile(
                "imagen", "foto.jpg", "image/jpeg", "fakeimage".getBytes()
        );

        mockMvc.perform(multipart("/inmuebles/alta")
                        .file(foto)
                        .session(session)
                        .param("titulo", "Casa bonita"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(gestorInmuebles).registrarInmueble(any(Inmueble.class));
    }

    // -------------------------------------------------------------------------
    // 3. POST /alta (usuario no propietario)
    // -------------------------------------------------------------------------

    @Test
    void testRegistrarInmuebleUsuarioNoPropietario() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorUsuarios.esPropietario("juan")).thenReturn(false);

        mockMvc.perform(multipart("/inmuebles/alta")
                        .file(new MockMultipartFile("imagen", new byte[0]))
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // -------------------------------------------------------------------------
    // 4. GET /{id}/foto (foto existente)
    // -------------------------------------------------------------------------

    @Test
    void testMostrarFotoConImagen() throws Exception {
        Inmueble inm = new Inmueble();
        inm.setFoto("fakeimage".getBytes()); // contenido sin encabezado JPEG

        when(gestorInmuebles.obtenerInmueblePorId(1L)).thenReturn(inm);

        mockMvc.perform(get("/inmuebles/1/foto"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_OCTET_STREAM));
    }

    // -------------------------------------------------------------------------
    // 6. GET /listar
    // -------------------------------------------------------------------------

    @Test
    void testListarInmuebles() throws Exception {
        mockMvc.perform(get("/inmuebles/listar"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("inmuebles"))
                .andExpect(view().name("listarInmuebles"));
    }

    // -------------------------------------------------------------------------
    // 7. DELETE /eliminar/{id}
    // -------------------------------------------------------------------------

    @Test
    void testEliminarInmueble() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(gestorInmuebles.eliminarInmueble(5L, "juan")).thenReturn(true);

        mockMvc.perform(delete("/inmuebles/eliminar/5").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.id").value(5));
    }
}
