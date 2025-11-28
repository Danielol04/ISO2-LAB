package com.nnm.nnm.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

@Controller
@RequestMapping("/pago")
public class VentanaPago {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    
    @GetMapping("/pago")
    public String mostrarPago(@RequestParam("idReserva") Long idReserva, Model model) {
        Reserva reserva = gestorReservas.obtenerReservaPorId(idReserva);
        if (reserva == null) {
            return "redirect:/home";
        }
        model.addAttribute("reserva", reserva);
        return "pago"; // vista donde el usuario realiza el pago
    }
}
