package com.nnm.nnm.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@Controller
@RequestMapping("/registro")
public class VentanaRegistroPropietario extends VentanaRegistro {

    @GetMapping("/propietario")
    public String mostrarFormulario(Model model) {
        model.addAttribute("propietario", new Propietario());
        return "propietario";
    }

    @PostMapping("/propietario")
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
