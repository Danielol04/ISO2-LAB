package com.nnm.nnm.presentacion;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.SolicitudReserva;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitudes")
public class VentanaSolicitudes {
    @Autowired
    private GestorSolicitudes gestorSolicitudes;

    @GetMapping("/confirmacionReserva/{username}")
    public String verSolicitudes(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username == null) {
            return "redirect:/login";
        }
        List<SolicitudReserva> solicitudes = gestorSolicitudes.obtenerSolicitudesPorPropietario(username);
        model.addAttribute("username", username);
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("solicitudSeleccionada", null);
        return "solicitudes";
    }

    @GetMapping("/confirmacionReserva/{username}/ver/{id}")
    public String verDetalleSolicitud(@PathVariable String username,
                                      @PathVariable Long id,
                                      Model model) {
        List<SolicitudReserva> solicitudes = gestorSolicitudes.obtenerSolicitudesPorPropietario(username);
        SolicitudReserva solicitudSeleccionada = gestorSolicitudes.obtenerSolicitudPorId(id);

        model.addAttribute("username", username);
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("solicitudSeleccionada", solicitudSeleccionada);

        return "solicitudes";
    }

    @PostMapping("/solicitud/{id}/aceptar")
    public String aceptarSolicitud(@PathVariable Long id) {
        gestorSolicitudes.aceptarSolicitudReserva(id);
        return "redirect:/confirmacionReserva";
    }

    @PostMapping("/solicitud/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable Long id) {
        gestorSolicitudes.rechazarSolicitudReserva(id);
        return "redirect:/confirmacionReserva";
    }
}