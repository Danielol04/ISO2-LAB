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

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
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
    private GestorInmuebles gestorInmuebles;

    @Autowired
    private GestorUsuarios gestorUsuarios;

    @GetMapping("/crear/{idInmueble}")
    public String mostrarFormularioReserva(@PathVariable Long idInmueble, Model model) {
        List<Disponibilidad> disponibilidades = gestorDisponibilidad.obtenerDisponibilidadPorInmueble(idInmueble);
        List<String> fechasDisponibles = new ArrayList<>();
        for (Disponibilidad d : disponibilidades) {
            LocalDate fecha = d.getFechaInicio();
            while (!fecha.isAfter(d.getFechaFin())) {
                fechasDisponibles.add(fecha.toString());
                fecha = fecha.plusDays(1);
            }
        }
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("idInmueble", idInmueble);
        model.addAttribute("fechasDisponibles", fechasDisponibles);

        return "reserva";
    }

    @PostMapping("/crear/{idInmueble}")
    public String crearReserva(@RequestParam long idInmueble, @ModelAttribute Reserva reserva, Model model,
            HttpSession session) {
            String usernamePropietario = (String) session.getAttribute("username");

        if (gestorUsuarios.esPropietario(usernamePropietario)) {
            log.warn("No se encontró el username del inquilino en la sesión");
            return "redirect:/login";
        } else {
            Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);
            reserva.setInquilino(gestorUsuarios.obtenerInquilinoPorUsername(usernamePropietario));
            reserva.setInmueble(inmueble);
            PoliticaCancelacion politica = gestorDisponibilidad.obtenerPoliticaCancelacion(
                    reserva.getInmueble().getId(), reserva.getFechaInicio(), reserva.getFechaFin());
            reserva.setPoliticaCancelacion(politica);
            gestorReservas.registrarReserva(reserva);
            long noches = ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
            double precioTotal = noches * reserva.getInmueble().getPrecio_noche();
            model.addAttribute("precioTotal", precioTotal);
            model.addAttribute("idReserva", reserva.getId());
            log.info("Reserva creada con ID: " + reserva.getId());
            return "redirect:/pago/confirmarPago/"+reserva.getId();
        }
    }

}
