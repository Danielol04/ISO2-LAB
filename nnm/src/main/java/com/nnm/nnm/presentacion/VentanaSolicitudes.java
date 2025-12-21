package com.nnm.nnm.presentacion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitudes")
public class VentanaSolicitudes {
    private static final Logger log = LoggerFactory.getLogger(VentanaSolicitudes.class);
    private static final String USERNAME = "username";

    private final GestorSolicitudes gestorSolicitudes;
    private final GestorPagos gestorPagos;
    private final GestorReservas gestorReservas;

    @Autowired
    public VentanaSolicitudes(GestorSolicitudes gestorSolicitudes, GestorPagos gestorPagos, GestorReservas gestorReservas) {
        this.gestorSolicitudes = gestorSolicitudes;
        this.gestorPagos = gestorPagos;
        this.gestorReservas = gestorReservas;
    }

    @GetMapping("/confirmacionReserva/{username}")
    public String verSolicitudes(@PathVariable String username, Model model, HttpSession session) {
        String sessionUsername = (String) session.getAttribute(USERNAME);
        if (sessionUsername == null || !sessionUsername.equals(username)) {
            return "redirect:/login";
        }

        List<SolicitudReserva> solicitudes = gestorSolicitudes.obtenerSolicitudesPorPropietario(username);
        model.addAttribute(USERNAME, username);
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("solicitudSeleccionada", null);
        return "solicitudes";
    }

    @PostMapping("/solicitud/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable Long id, HttpSession session) {
        gestorSolicitudes.aceptarSolicitudReserva(id);
        String username = (String) session.getAttribute(USERNAME);
        return "redirect:/solicitudes/confirmacionReserva/" + username;
    }

    @PostMapping("/solicitud/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable Long id, HttpSession session) {
        SolicitudReserva solicitud = gestorSolicitudes.obtenerSolicitudPorId(id);
        log.info("Rechazando solicitud con ID: {}", id);
        gestorSolicitudes.borrarSolicitudReserva(solicitud);
        Reserva reserva = solicitud.getReserva();
        log.info("Cancelando reserva asociada con ID: {} " + reserva.getId());
        gestorReservas.cancelarReserva(reserva.getId());

        String username = (String) session.getAttribute(USERNAME);
        return "redirect:/solicitudes/confirmacionReserva/" + username;
    }
}
