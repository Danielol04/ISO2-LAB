package com.nnm.nnm.presentacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

@Controller
@RequestMapping("/pago")
public class VentanaPago {
    Logger log = LoggerFactory.getLogger(VentanaPago.class);

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @GetMapping("/confirmarPago/{idReserva}")
    public String mostrarPago(@PathVariable("idReserva") Long idReserva, Model model) {
        Reserva reserva = gestorReservas.obtenerReservaPorId(idReserva);
        if (reserva == null|| reserva.getPagado()) {
            return "redirect:/home";
        }
        model.addAttribute("reserva", reserva);
        return "pago";
    }

    @PostMapping("/confirmarPago/{idReserva}")
    public String confirmarPago(@RequestParam("idReserva") Long idReserva , Model model, RedirectAttributes redirectAttrs) {
        Reserva reserva = gestorReservas.obtenerReservaPorId(idReserva);
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(reserva.getInmueble().getId());
        reserva.setInmueble(inmueble);
        reserva.setPagado(true);
        Boolean reservaDirecta = gestorDisponibilidad.obtenerTipoReserva(reserva.getInmueble().getId(),reserva.getFechaInicio(), reserva.getFechaFin());
        if (reservaDirecta) {
            gestorDisponibilidad.actualizarDisponibilidadPorReserva(
                    reserva.getInmueble().getId(),
                    reserva.getFechaInicio(),
                    reserva.getFechaFin());
            redirectAttrs.addFlashAttribute("mensajePopup", "Reserva realizada con éxito");
        } else {
            gestorReservas.generarSolicitudReserva(reserva);
            redirectAttrs.addFlashAttribute("mensajePopup",
                    "Solicitud de reserva enviada, espere la respuesta por parte del propietario");
        }
        gestorReservas.actualizarReserva(reserva);
        model.addAttribute("idInmueble", reserva.getInmueble().getId());
        return "redirect:/reserva/crear/" + reserva.getInmueble().getId();
    }
}
