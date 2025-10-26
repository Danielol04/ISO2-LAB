package com.nnm.nnm.presentacion;
import java.security.Principal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nnm.nnm.negocio.controller.GestorReservas;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.negocio.dominio.entidades.Usuario;
import com.nnm.nnm.persistencia.UsuarioDAO;

@Controller
@RequestMapping("/Reserva")
public class VentanaReserva {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/formulario")
    public String mostrarFormularioReserva(Model model) {
        model.addAttribute("reserva", new Reserva());
        return "NuevaReserva"; // nombre de la vista del formulario
    }

    @PostMapping("/crearReserva")
    public String crearReserva(@ModelAttribute Reserva reserva,
        @RequestParam("idInmueble") Inmueble idInmueble,
        @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
        @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin,
        @RequestParam("politicaCancelacion") String politicaCancelacion,
        Principal principal, // usuario logueado
        Model model) {
        
        String username= principal.getName();
        Usuario usuario= InquilinoDAO.findbyUsername(username);


        reserva.setId_inmueble(idInmueble);
        reserva.setFecha_inicio(fechaInicio);
        reserva.setFecha_fin(fechaFin);
        reserva.setPolitica_cancelacion(politicaCancelacion);
        reserva.setUsername_inquilino(usuario);

        gestorReservas.registrarReserva(reserva);
        return "reserva_result"; // página de confirmación
    }
}
