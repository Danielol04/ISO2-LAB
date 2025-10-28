package com.nnm.nnm.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;

@Controller
@RequestMapping("/disponibilidad")
public class VentanaDisponibilidad {

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;
/*
    @GetMapping("/{id_inmueble}")
    public String consultar(@PathVariable long id_inmueble, Model model) {
        model.addAttribute("disponibilidades",
                gestorDisponibilidad.obtenerDisponibilidadPorInmueble(id_inmueble));
        model.addAttribute("id_inmueble", id_inmueble);
        return "disponibilidad";

    }
*/

}
