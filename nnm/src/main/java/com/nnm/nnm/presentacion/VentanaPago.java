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
import com.nnm.nnm.negocio.controller.GestorSolicitudes;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

@Controller
@RequestMapping("/pago")
public class VentanaPago {
    Logger log = LoggerFactory.getLogger(VentanaPago.class);

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorSolicitudes gestorSolicitudes;

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @GetMapping("/confirmarPago/{idReserva}")
    public String mostrarPago(@PathVariable("idReserva") Long idReserva,Double precioTotal, Model model) {
        Reserva reserva = gestorReservas.obtenerReservaPorId(idReserva);
        if (reserva == null|| reserva.getPagado()) {
            return "redirect:/home";
        }
        model.addAttribute("reserva", reserva);
        return "pago";
    }

    @PostMapping("/confirmarPago/{idReserva}")
    public String confirmarPago(@PathVariable("idReserva") Long idReserva, @RequestParam Double precioTotal , Model model, RedirectAttributes redirectAttrs) {
        log.info("Confirmando pago para la reserva ID: " + idReserva);
        Reserva reserva = gestorReservas.obtenerReservaPorId(idReserva);
        Long idInmueble= reserva.getInmueble().getId();
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
        reserva.setInmueble(inmueble);
        Boolean reservaDirecta = gestorDisponibilidad.obtenerTipoReserva(idInmueble,reserva.getFechaInicio(), reserva.getFechaFin());
        if (reservaDirecta) {
            log.info("Reserva directa para la reserva ID: " + idReserva);
            gestorDisponibilidad.actualizarDisponibilidadPorReserva(
                    reserva.getInmueble().getId(),
                    reserva.getFechaInicio(),
                    reserva.getFechaFin());
            redirectAttrs.addFlashAttribute("mensajePopup", "Reserva realizada con éxito");
        } else {
            log.info("Solicitud de reserva para la reserva ID: " + idReserva);
            gestorSolicitudes.generarSolicitudReserva(reserva,precioTotal);
            redirectAttrs.addFlashAttribute("mensajePopup","Solicitud de reserva enviada, espere la respuesta por parte del propietario");
        }
        log.info("Marcando la reserva como pagada para la reserva ID: " + idReserva);
        reserva.setPagado(true);
        gestorReservas.actualizarReserva(reserva);
        model.addAttribute("idInmueble", idInmueble);
        return "redirect:/reserva/crear/"+idInmueble;
    }
}