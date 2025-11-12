package com.nnm.nnm.presentacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String mostrarFormulario(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        log.info("Mostrando formulario de AltaInmueble");
        return "AltaInmuebles";
    }

    @PostMapping("/alta")
    public String registrar(@ModelAttribute Inmueble inmueble, HttpSession session/*,@RequestParam("foto") MultipartFile foto */) {
        try {
            /* 
            if (!foto.isEmpty()) {
                inmueble.setFoto(foto.getBytes());
            }
            */
            String usernamePropietario = (String) session.getAttribute("username");
            if(gestorUsuarios.esPropietario(usernamePropietario)) {
                log.warn("No se encontró el username del propietario en la sesión");
                return "redirect:/login"; // Redirigir al login si no hay usuario en sesión
            }
            Propietario propietario = gestorUsuarios.obtenerPropietarioPorUsername(usernamePropietario);
            inmueble.setUsername_propietario(propietario);
            gestorInmuebles.registrarInmueble(inmueble);
            log.info("Inmueble registrado: {}", inmueble.getTitulo());
        } catch (Exception e) {
            log.error("Error al guardar la foto del inmueble", e);
            // manejar el error según tu necesidad
        }
        return "redirect:/home";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("inmuebles", gestorInmuebles.listarInmuebles());
        return "listarInmuebles";
    }
}
