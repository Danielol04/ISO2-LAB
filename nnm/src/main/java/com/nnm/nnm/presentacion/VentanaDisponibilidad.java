package com.nnm.nnm.presentacion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/disponibilidades")
public class VentanaDisponibilidad {
     private static final Logger log = LoggerFactory.getLogger(VentanaDisponibilidad.class);

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;
    @Autowired
    private GestorInmuebles gestorInmuebles;
    
    @GetMapping("/crear/{id}")
    public String mostrarFormulario(@PathVariable long id ,Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username == null) {
                return "redirect:/login";
            }
                
            model.addAttribute("disponibilidad", new Disponibilidad());
            model.addAttribute("politicas", PoliticaCancelacion.values());
            model.addAttribute("idInmueble", id);
            log.info("Mostrando formulario de Disponibilidad para el inmueble: {}", id);
            return "Disponibilidad";
        }
        @PostMapping("/crear/{id}")
        public String crearDisponibilidad(@PathVariable long id, @ModelAttribute("disponibilidad") Disponibilidad disponibilidad, HttpSession session, Model model) {

            String username = (String) session.getAttribute("username");
            if (username == null) {
                return "redirect:/login";
            }
            String mensajeError = "";
            Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(id);
            if (inmueble == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inmueble no encontrado");
            }
            
            if (!inmueble.getPropietario().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso");
            }

            disponibilidad.setInmueble(inmueble);

            if(disponibilidad.getFechaFin().isBefore(disponibilidad.getFechaInicio())) {
                mensajeError = "La fecha de fin no puede ser anterior a la fecha de inicio.";
                errorDisponibilidad(model, mensajeError, id);
                return "Disponibilidad";
            }
            
            // Validar que no haya solapamiento con otras disponibilidades del mismo inmueble
            List<Disponibilidad> disponibilidadesExistentes = gestorDisponibilidad.obtenerDisponibilidadPorInmueble(id);
            boolean solapa = disponibilidadesExistentes.stream().anyMatch(d ->
                !d.getFechaFin().isBefore(disponibilidad.getFechaInicio()) &&
                !d.getFechaInicio().isAfter(disponibilidad.getFechaFin())
            );

            if (solapa) {
                mensajeError = "Las fechas se solapan con una disponibilidad existente.";
                errorDisponibilidad(model, mensajeError, id);
                return "Disponibilidad";
            }

            log.info("Creando disponibilidad para el inmueble: {}", id);
            gestorDisponibilidad.registrarDisponibilidad(disponibilidad);
            return "redirect:/home";
        }

        private void errorDisponibilidad(Model model, String mensajeError, long id) {
            model.addAttribute("error", mensajeError);
            model.addAttribute("politicas", PoliticaCancelacion.values());
            model.addAttribute("idInmueble", id);
        }
    

}
