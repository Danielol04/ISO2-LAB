package com.nnm.nnm.presentacion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/inmuebles")
public class VentanaInmueble{

    private static final Logger log = LoggerFactory.getLogger(VentanaInmueble.class);

    @Autowired
    private GestorInmuebles gestorInmuebles;
    @Autowired
    private GestorUsuarios gestorUsuarios;

    @GetMapping("/alta")
    public String mostrarFormulario(Model model, HttpSession session) {
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("inmueble", new Inmueble());
        log.info("Mostrando formulario de AltaInmueble");
        return "AltaInmuebles";
    }

    @PostMapping("/alta")
    public String registrar(@ModelAttribute Inmueble inmueble, HttpSession session,@RequestParam("imagen") MultipartFile foto ) {
        try {
            
            if (!foto.isEmpty()) {
                inmueble.setFoto(foto.getBytes());
            }
            
            String usernamePropietario = (String) session.getAttribute("username");
            if(gestorUsuarios.esPropietario(usernamePropietario)) {
                
                Propietario propietario = gestorUsuarios.obtenerPropietarioPorUsername(usernamePropietario);
                inmueble.setUsername_propietario(propietario);
                gestorInmuebles.registrarInmueble(inmueble);
                log.info("Inmueble registrado: {}", inmueble.getTitulo());
                return"redirect:/home";
            }else{
                log.warn("No se encontró el username del propietario en la sesión");
                return "redirect:/login"; // Redirigir al login si no hay usuario en sesión
            }
        } catch (IOException e) {
            log.error("Error al guardar la foto del inmueble", e);
            // manejar el error según tu necesidad
        }
        return "redirect:/home";
    }

    @GetMapping("/{id}/foto")
    @ResponseBody
    public ResponseEntity<byte[]> mostrarFoto(@PathVariable Long id) {
        byte[] imagenBytes;
        String tipoMime;

        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(id);

        if (inmueble == null || inmueble.getFoto() == null) {
            // Cargar imagen genérica desde recursos
            try (InputStream is = getClass().getResourceAsStream("/static/images/foto-NotFound.jpg")) {
                if (is == null) {
                    return ResponseEntity.notFound().build();
                }
                imagenBytes = is.readAllBytes();
                tipoMime = "image/jpg";
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            imagenBytes = inmueble.getFoto();
            try (InputStream is = new ByteArrayInputStream(imagenBytes)) {
                String mime = URLConnection.guessContentTypeFromStream(is);
                tipoMime = mime != null ? mime : "application/octet-stream";
            } catch (IOException e) {
                tipoMime = "application/octet-stream";
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(tipoMime));
        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
    }


    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("inmuebles", gestorInmuebles.listarInmuebles());
        return "listarInmuebles";
    }
    
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarInmueble(@PathVariable Long id, HttpSession session) {
        String usernamePropietario = (String) session.getAttribute("username");
        boolean eliminado = gestorInmuebles.eliminarInmueble(id, usernamePropietario);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("exito", eliminado);
        respuesta.put("id", id);

        log.info("Eliminación de inmueble con id {}: {}", id, eliminado ? "éxito" : "fallo");

        return ResponseEntity.ok(respuesta);
    }
}
