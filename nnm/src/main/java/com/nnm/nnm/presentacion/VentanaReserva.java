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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.InquilinoDAO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/reserva")
public class VentanaReserva {

    private static final Logger log = LoggerFactory.getLogger(VentanaReserva.class);

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @GetMapping("/crearReserva")
    public String mostrarFormularioReserva(@RequestParam("idInmueble") Long idInmueble, Model model) {
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
        model.addAttribute("inmueble", inmueble);
        return "reserva";
    }

    @PostMapping("/crearReserva")
    public String crearReserva(Reserva reserva, Model model, HttpSession session) {

        String usernamePropietario = (String) session.getAttribute("username");

        if (gestorUsuarios.esPropietario(usernamePropietario)) {
            log.warn("No se encontró el username del inquilino en la sesión");
            return "redirect:/login"; // Redirigir al login si no hay usuario en sesión
        } else {
            // Convertir el ID del inmueble a objeto Inmueble
            Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(reserva.getIdInmueble());
            reserva.setInmueble(inmueble);
            PoliticaCancelacion politica = gestorDisponibilidad.obtenerPoliticaCancelacion(reserva.getIdInmueble(),reserva.getFechaInicio(),reserva.getFechaFin());
            reserva.setPoliticaCancelacion(politica);
            model.addAttribute("reserva", reserva);
            gestorReservas.registrarReserva(reserva);
            return "pago";
        }
    }

    @PostMapping("/pago")
    public String confirmarPago(@ModelAttribute Reserva reserva, Model model, RedirectAttributes redirectAttrs) {

        // Convertir ID a objeto Inmueble
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(reserva.getIdInmueble());
        reserva.setInmueble(inmueble);

        Boolean reservaDirecta = gestorDisponibilidad.obtenerTipoReserva(reserva.getIdInmueble(),reserva.getFechaInicio(),reserva.getFechaFin());
        if (reservaDirecta) {
            gestorDisponibilidad.actualizarDisponibilidadPorReserva(
                    inmueble.getId(),
                    reserva.getFechaInicio(),
                    reserva.getFechaFin());
            redirectAttrs.addFlashAttribute("mensajePopup", "Reserva realizada con éxito");
        } else {
            gestorReservas.generarSolicitudReserva(reserva);
            redirectAttrs.addFlashAttribute("mensajePopup",
                    "Solicitud de reserva enviada, espere la respuesta por parte del propietario");
        }
        return "redirect:/home";
    }

    /*
     * @GetMapping("/cancelarReserva")
     * public String mostrarFormularioCancelarReserva(Model model) {
     * model.addAttribute("reserva", new Reserva());
     * return "cancelarReserva"; // nombre de la vista del formulario de cancelación
     * }
     * 
     * @PostMapping("/cancelarReserva")
     * public String cancelarReserva(Long idReserva, String usernameUsuario, Model
     * model) {
     * boolean exito = gestorReservas.cancelarReserva(idReserva, usernameUsuario);
     * if (exito) {
     * model.addAttribute("mensaje", "Reserva cancelada exitosamente");
     * } else {
     * model.addAttribute("mensaje", "Error al cancelar la reserva");
     * }
     * return "confirmacionCancelacion"; // nombre de la vista de confirmación de
     * cancelación
     * }
     */

}
