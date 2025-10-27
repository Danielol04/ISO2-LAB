package com.nnm.nnm.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

@Controller
@RequestMapping("/inmuebles")
public class VentanaInmueble{

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @GetMapping("/alta")
    public String mostrarFormulario(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        return "AltaInmuebles";
    }

    @PostMapping("/alta")
    public String registrar(@ModelAttribute Inmueble inmueble) {
        gestorInmuebles.registrarInmueble(inmueble);
        return "redirect:/inmuebles/listar";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("inmuebles", gestorInmuebles.listarInmuebles());
        return "listarInmuebles";
    }
}
