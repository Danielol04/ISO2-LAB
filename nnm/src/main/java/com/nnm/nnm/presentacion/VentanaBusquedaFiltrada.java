package com.nnm.nnm.presentacion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

@Controller
public class VentanaBusquedaFiltrada {

    @Autowired
    private GestorInmuebles gestorInmuebles;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String localidad,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer habitaciones,
            @RequestParam(required = false) Integer banos,
            Model model
    ) {
        LocalDate fechaInicio = null;
        LocalDate fechaFin = null;

        if (fechaDesde != null && !fechaDesde.isEmpty()) {
            fechaInicio = LocalDate.parse(fechaDesde, formatter);
        }
        if (fechaHasta != null && !fechaHasta.isEmpty()) {
            fechaFin = LocalDate.parse(fechaHasta, formatter);
        }

        List<Inmueble> resultados = gestorInmuebles.buscarFiltrado(
                localidad, provincia, titulo,
                fechaInicio, fechaFin,
                tipo, habitaciones, banos,
                null, null
        );

        model.addAttribute("propiedades", resultados);
        return "homeInquilino"; // Cambiar a "home" si quieres mostrar la versión genérica
    }
}
