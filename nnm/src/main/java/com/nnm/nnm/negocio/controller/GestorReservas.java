package com.nnm.nnm.negocio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;
import com.nnm.nnm.persistencia.ReservaDAO;
import com.nnm.nnm.persistencia.UsuarioDAO;



@Controller
public class GestorReservas {
    public static final Logger log = LoggerFactory.getLogger(GestorReservas.class);//Logger para poder hacer seguimiento de las acciones en el gestor de reservas

   @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private InmuebleDAO inmuebleDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/reservas")
    public String reservasForm(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("Propiedades", inmuebleDAO.findAll());
        model.addAttribute("Usuarios", usuarioDAO.findAll());
        log.info(reservaDAO.findAll().toString());
        return "reserva";//html
    }

    @PostMapping("/reservas")
    public String reservasSubmit(@ModelAttribute Reserva reserva, Model model) {
        model.addAttribute("reserva", reserva);
        Reserva savedReserva = reservaDAO.save(reserva);
        log.info("Reserva guardada: " + savedReserva);
        return "result";
    }

}

    