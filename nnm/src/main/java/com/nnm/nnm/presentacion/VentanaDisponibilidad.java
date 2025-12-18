package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/disponibilidades")
public class VentanaDisponibilidad {
    private static final Logger log = LoggerFactory.getLogger(VentanaDisponibilidad.class);

    private static final String USERNAME = "username";
    private static final String RETURNLOGING = "redirect:/login";
    private static final String RETURNCREAR = "redirect:/disponibilidades/crear/";

    private final GestorDisponibilidad gestorDisponibilidad;
    private final GestorInmuebles gestorInmuebles;
    private final GestorReservas gestorReservas;

    @Autowired
    public VentanaDisponibilidad(GestorDisponibilidad gestorDisponibilidad, GestorInmuebles gestorInmuebles, GestorReservas gestorReservas) {
        this.gestorDisponibilidad = gestorDisponibilidad;
        this.gestorInmuebles = gestorInmuebles;
        this.gestorReservas = gestorReservas;
    }

    @GetMapping("/crear/{id}")
    public String mostrarFormulario(@PathVariable long id, Model model, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return RETURNLOGING;
        }
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(id);
        List<Disponibilidad> disponibilidades = gestorDisponibilidad.obtenerDisponibilidadPorInmueble(id);
        List <Reserva> reservas = gestorReservas.obtenerReservasPorInmueble(id);
        model.addAttribute("disponibilidad", new Disponibilidad());
        model.addAttribute("politicas", PoliticaCancelacion.values());
        model.addAttribute("idInmueble", id);
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("disponibilidades", disponibilidades);

        List<String> fechasDisponibles = new ArrayList<>();
        for (Disponibilidad d : disponibilidades) {
            LocalDate fecha = d.getFechaInicio();
            while (!fecha.isAfter(d.getFechaFin())) {
                fechasDisponibles.add(fecha.toString());
                fecha = fecha.plusDays(1);
            }
        }
        List<String> fechasReservadas = new ArrayList<>();
        for(Reserva reserva: reservas){
            reserva.getEstado(); // Actualiza el estado de la reserva
            if (reserva.getEstado().equals(EstadoReserva.EXPIRADA) || !reserva.getPagado()) {
                gestorReservas.cancelarReserva(reserva.getId());
                continue;
            }
            LocalDate fecha = reserva.getFechaInicio();
            while (!fecha.isAfter(reserva.getFechaFin())) {
                fechasReservadas.add(fecha.toString());
                fecha = fecha.plusDays(1);
            }
        }
        model.addAttribute("fechasReservadas", fechasReservadas);
        model.addAttribute("fechasDisponibles", fechasDisponibles);
        model.addAttribute(USERNAME, username);

        log.info("Mostrando formulario de Disponibilidad para el inmueble: {}", id);
        return "disponibilidad";
    }

    @PostMapping("/crear/{id}")
    public String crearDisponibilidad(@PathVariable long id, @ModelAttribute("disponibilidad") Disponibilidad disponibilidad, HttpSession session, Model model) {

        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return RETURNLOGING;
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

        if (disponibilidad.getFechaFin().isBefore(disponibilidad.getFechaInicio())) {
            mensajeError = "La fecha de fin no puede ser anterior a la fecha de inicio.";
            errorDisponibilidad(model, mensajeError, id);
            
            return RETURNCREAR + id;
        }

        // Validar que no haya solapamiento con otras disponibilidades del mismo inmueble
        List<Disponibilidad> disponibilidadesExistentes = gestorDisponibilidad.obtenerDisponibilidadPorInmueble(id);
        boolean solapa = disponibilidadesExistentes.stream()
                .anyMatch(d -> !d.getFechaFin().isBefore(disponibilidad.getFechaInicio()) &&
                        !d.getFechaInicio().isAfter(disponibilidad.getFechaFin()));

        if (solapa) {
            mensajeError = "Las fechas se solapan con una disponibilidad existente.";
            errorDisponibilidad(model, mensajeError, id);
            return RETURNCREAR + id;
        }

        log.info("Creando disponibilidad para el inmueble: {}", id);
        gestorDisponibilidad.registrarDisponibilidad(disponibilidad);
        return RETURNCREAR + id;
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable long id, HttpSession session) {


        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return RETURNLOGING;
        }
        Disponibilidad disponibilidad = gestorDisponibilidad.obtenerDisponibilidadPorId(id);
        if (disponibilidad == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disponibilidad no encontrada");
        }

        // verificar propietario
        if (!disponibilidad.getInmueble().getPropietario().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso");
        }
        long idInmueble = disponibilidad.getInmueble().getId();

        gestorDisponibilidad.eliminarDisponibilidad(disponibilidad);

        return RETURNCREAR + idInmueble;
    }

    private void errorDisponibilidad(Model model, String mensajeError, long id) {
        model.addAttribute("error", mensajeError);
        model.addAttribute("politicas", PoliticaCancelacion.values());
        model.addAttribute("idInmueble", id);
    }

}