package com.nnm.nnm.presentacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;


@Controller
@RequestMapping("/reserva")
public class VentanaReserva {

    @Autowired
    private GestorReservas gestorReservas;

    

    @GetMapping("/crearReserva")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "nuevaReserva"; // nombre de la vista del formulario
    }

    @PostMapping("/crearReserva")
    public String crearReserva(Reserva reserva, Model model) {
        gestorReservas.registrarReserva(reserva);
        model.addAttribute("mensaje", "Reserva creada exitosamente");
        return "confirmacionReserva"; // nombre de la vista de confirmación
    }

    /*@GetMapping("/cancelarReserva")
    public String mostrarFormularioCancelarReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "cancelarReserva"; // nombre de la vista del formulario de cancelación
    }
    @PostMapping("/cancelarReserva")
    public String cancelarReserva(Long idReserva, String usernameUsuario, Model model) {
        boolean exito = gestorReservas.cancelarReserva(idReserva, usernameUsuario);
        if (exito) {
            model.addAttribute("mensaje", "Reserva cancelada exitosamente");
        } else {
            model.addAttribute("mensaje", "Error al cancelar la reserva");
        }
        return "confirmacionCancelacion"; // nombre de la vista de confirmación de cancelación
    }*/

}
