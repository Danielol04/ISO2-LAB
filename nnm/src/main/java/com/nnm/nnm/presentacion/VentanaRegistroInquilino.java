package com.nnm.nnm.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.dominio.entidades.Inquilino;

@Controller
@RequestMapping("/registro")
public class VentanaRegistroInquilino extends VentanaRegistro {

    @GetMapping("/inquilino")
    public String mostrarFormulario(Model model) {
        model.addAttribute("inquilino", new Inquilino());
        return "inquilino";
    }

    @PostMapping("/inquilino")
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

    
