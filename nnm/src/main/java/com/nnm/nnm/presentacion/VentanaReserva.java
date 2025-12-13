package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorPagos;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.EstadoReserva;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;

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
    private GestorPagos gestorPago;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @GetMapping("/crear/{idInmueble}")
    public String mostrarFormularioReserva(@PathVariable Long idInmueble, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        List<Disponibilidad> disponibilidades = gestorDisponibilidad.obtenerDisponibilidadPorInmueble(idInmueble);
        List<Reserva> reservas = gestorReservas.obtenerReservasPorInmueble(idInmueble);
        List<String> fechasDisponibles = new ArrayList<>();
        for (Disponibilidad d : disponibilidades) {
            LocalDate fecha = d.getFechaInicio();
            while (!fecha.isAfter(d.getFechaFin())) {
                fechasDisponibles.add(fecha.toString());
                fecha = fecha.plusDays(1);
            }
        }
        List<String> fechasReservadas = new ArrayList<>();
        for (Reserva reserva : reservas) {
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
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("idInmueble", idInmueble);
        model.addAttribute("fechasDisponibles", fechasDisponibles);
        model.addAttribute("fechasReservadas", fechasReservadas);
        model.addAttribute("username", username);

        return "reserva";
    }

    @PostMapping("/crear/{idInmueble}")
    public String crearReserva(@RequestParam long idInmueble, @ModelAttribute Reserva reserva, Model model,
            HttpSession session, RedirectAttributes redirectAttrs) {
        String username = (String) session.getAttribute("username");

        if (gestorUsuarios.esPropietario(username) || username == null) {
            log.warn("No se encontró el username del inquilino en la sesión");
            return "redirect:/login";
        } else {
            Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
            reserva.setInquilino(gestorUsuarios.obtenerInquilinoPorUsername(username));
            reserva.setInmueble(inmueble);
            List<Disponibilidad> afectadas = gestorDisponibilidad.obtenerDisponibilidadParaReserva(idInmueble,
                    reserva.getFechaInicio(), reserva.getFechaFin());
            PoliticaCancelacion politica;
            boolean reservaDirecta;
            log.info("Disponibilidades afectadas:");
            for (Disponibilidad d : afectadas) {
                log.info(" - " + d.getFechaInicio() + " a " + d.getFechaFin() + " | Politica: "+ d.getPoliticaCancelacion());
            }
            log.info("Total afectadas = " + afectadas.size());
            if (afectadas.size() == 1) {
                politica = afectadas.get(0).getPoliticaCancelacion();
                reserva.setPoliticaCancelacion(politica);
                reservaDirecta = afectadas.get(0).getReservaDirecta();
            } else {
                politica = gestorDisponibilidad.calcularPoliticaCancelacion(afectadas);
                reservaDirecta = gestorDisponibilidad.calcularTipoReserva(afectadas);
            }
            reserva.setPoliticaCancelacion(politica);
            reserva.setReservaDirecta(reservaDirecta);
            gestorReservas.registrarReserva(reserva);
            long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            double precioTotal = noches * reserva.getInmueble().getPrecio_noche();
            redirectAttrs.addFlashAttribute("precioTotal", precioTotal);
            model.addAttribute("idReserva", reserva.getId());
            if (reserva.getId() == null) {
                log.error("Error al crear la reserva, ID nulo");
                return "redirect:/reserva/crear/" + idInmueble;
            }
            log.info("Reserva creada con ID: " + reserva.getId());
            return "redirect:/pago/confirmarPago/" + reserva.getId();
        }
    }

    @GetMapping("/misReservas/{username}")
    public String verMisReservas(@PathVariable String username, Model model, HttpSession session) {
        String usernameSession = (String) session.getAttribute("username");
        if (usernameSession == null || !usernameSession.equals(username)) {
            return "redirect:/login";
        }
        List<Reserva> reservas = new ArrayList<>();
        boolean cancelacionesRealizadas = false;
        boolean esPropietario = gestorUsuarios.esPropietario(username);
        if (esPropietario) {
            reservas = gestorReservas.obtenerReservasPorPropietario(username);
        } else {
            reservas = gestorReservas.obtenerReservasPorInquilino(username);
        }
        for (Reserva reserva : reservas) {
            reserva.getEstado(); // Actualiza el estado de la reserva
            if (reserva.getEstado().equals(EstadoReserva.EXPIRADA) || !reserva.getPagado()) {
                gestorReservas.cancelarReserva(reserva.getId());
                cancelacionesRealizadas = true;
            }
        }
        if (cancelacionesRealizadas) {
            if (gestorUsuarios.esInquilino(username)) {
                reservas = gestorReservas.obtenerReservasPorInquilino(username);
            } else {
                reservas = gestorReservas.obtenerReservasPorPropietario(username);
            }
        }
        model.addAttribute("esPropietario",esPropietario);
        model.addAttribute("reservas", reservas);
        model.addAttribute("username", username);
        return "misReservas";
    }

    @PostMapping("/cancelar/{idReserva}")
    public String cancelarReserva(@PathVariable Long idReserva, HttpSession session, RedirectAttributes redirectAttrs) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Pago pago = gestorPago.obtenerPagoPorReserva(idReserva);
        gestorPago.borrarPago(pago);
        gestorReservas.cancelarReserva(idReserva);
        return "redirect:/reserva/misReservas/" + username;
    }
}