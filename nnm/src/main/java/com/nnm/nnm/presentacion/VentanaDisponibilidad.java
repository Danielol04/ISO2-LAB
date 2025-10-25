package com.nnm.nnm.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import java.util.List;

@Controller
@RequestMapping("/Disponibilidad")
public class VentanaDisponibilidad {

    @Autowired
    private GestorDisponibilidad gestorDisponibilidad;

    @Autowired
    private GestorInmuebles gestorInmuebles;

    // Mostrar formulario para nueva disponibilidad
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("disponibilidad", new Disponibilidad());
        List<Inmueble> inmuebles = gestorInmuebles.listarInmuebles();
        model.addAttribute("inmuebles", inmuebles);
        return "altaDisponibilidad";
    }

    // Procesar alta o edición de disponibilidad
    @PostMapping("/guardar")
    public String procesarDisponibilidad(@ModelAttribute("disponibilidad") Disponibilidad disponibilidad, Model model) {
        try {
            if(disponibilidad.getId_inmueble() <= 0) {
                model.addAttribute("error", "Debe seleccionar un inmueble válido");
                return "altaDisponibilidad";
            }
            if(disponibilidad.getFecha_inicio() == null || disponibilidad.getFecha_fin() == null) {
                model.addAttribute("error", "Debe indicar fecha de inicio y fin");
                return "altaDisponibilidad";
            }

            gestorDisponibilidad.guardarDisponibilidad(disponibilidad);
            model.addAttribute("mensaje", "Disponibilidad guardada correctamente");
            return "resultadoDisponibilidad";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar disponibilidad: " + e.getMessage());
            return "altaDisponibilidad";
        }
    }

    // Listar disponibilidades
    @GetMapping("/listar")
    public String listarDisponibilidades(Model model) {
        model.addAttribute("disponibilidades", gestorDisponibilidad.listarDisponibilidades());
        return "listarDisponibilidades";
    }

    // Editar disponibilidad
    @GetMapping("/editar/{id}")
    public String editarDisponibilidad(@PathVariable long id, Model model) {
        Disponibilidad d = gestorDisponibilidad.obtenerDisponibilidad(id);
        if(d == null) {
            model.addAttribute("error", "Disponibilidad no encontrada");
            return "listarDisponibilidades";
        }
        model.addAttribute("disponibilidad", d);
        List<Inmueble> inmuebles = gestorInmuebles.listarInmuebles();
        model.addAttribute("inmuebles", inmuebles);
        return "altaDisponibilidad";
    }

    // Eliminar disponibilidad
    @GetMapping("/eliminar/{id}")
    public String eliminarDisponibilidad(@PathVariable long id, Model model) {
        gestorDisponibilidad.eliminarDisponibilidad(id);
        return "redirect:/Disponibilidad/listar";
    }
}
