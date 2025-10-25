package com.nnm.nnm.presentacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@Controller
public class VentanaRegistroPropietario {
    private  static final Logger log = LoggerFactory.getLogger(VentanaRegistroPropietario.class);
    
    @Autowired
    private GestorUsuarios gestorUsuarios; 

    @GetMapping("/registro/propietario")
    public String mostrarFormulario(Model model) {
        log.info("Mostrando formulario de propietario");
        model.addAttribute("propietario", new Propietario());
        return "propietario";
    }

    @PostMapping("/registro/propietario")
    public String procesarRegistro(@ModelAttribute Propietario propietario, Model model) {
        if (gestorUsuarios.existeUsuario(propietario.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "propietario";
        }

        gestorUsuarios.registrarPropietario(propietario);
        model.addAttribute("mensaje", "Propietario registrado correctamente");
        log.info("Propietario registrado: {}", propietario.getUsername());
        return "resultadoRegistro";
    }
}
