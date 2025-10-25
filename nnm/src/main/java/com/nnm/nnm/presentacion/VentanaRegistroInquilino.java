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
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;

@Controller
public class VentanaRegistroInquilino {
    private  static final Logger log = LoggerFactory.getLogger(VentanaRegistroInquilino.class);
    
    @Autowired
    private GestorUsuarios gestorUsuarios; 

    @GetMapping("/registro/inquilino")
    public String mostrarFormulario(Model model) {
        log.info("Mostrando formulario de inquilino");
        model.addAttribute("inquilino", new Inquilino());
        return "inquilino";
    }

    @PostMapping("/registro/inquilino")
    public String procesarRegistro(@ModelAttribute Inquilino inquilino, Model model) {
        if (gestorUsuarios.existeUsuario(inquilino.getUsername())) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return "inquilino";
        }
        gestorUsuarios.registrarInquilino(inquilino);
        model.addAttribute("mensaje", "Inquilino registrado correctamente");
        log.info("Inquilino registrado: {}", inquilino.getUsername());
        return "resultadoRegistro";
    }
}

    
