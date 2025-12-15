package com.nnm.nnm.presentacion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nnm.nnm.negocio.controller.GestorBusquedas;
import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import jakarta.servlet.http.HttpSession;

@Controller
public class VentanaHome {

    private static final Logger log = LoggerFactory.getLogger(VentanaHome.class);

    @Autowired
    private GestorInmuebles gestorInmuebles;

    @Autowired
    private GestorBusquedas gestorBusquedas;

    @Autowired
    private GestorUsuarios gestorUsuarios;

    /**
     * Home NORMAL = /home
     */
    @GetMapping("/home")
    public String mostrarHome(Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        List<Inmueble> propiedades;

        if (gestorUsuarios.esPropietario(username)) {
            log.info("Home propietario");
            propiedades = gestorInmuebles.listarPorPropietario(username);
            model.addAttribute("propiedades", propiedades);
            return "homePropietario";

        } else if (gestorUsuarios.esInquilino(username)) {
            log.info("Home inquilino");
            propiedades = gestorInmuebles.listarInmuebles();
            model.addAttribute("propiedades", propiedades);
            return "homeInquilino";

        } else {
            log.info("Home público");
            propiedades = gestorInmuebles.listarInmuebles();
            model.addAttribute("propiedades", propiedades);
            return "home";
        }
    }

    /**
     * Búsqueda filtrada = /buscar
     */
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) Integer habitaciones,
            @RequestParam(required = false) Integer banos,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model,
            HttpSession session) {

        String username = (String) session.getAttribute("username");

        List<Inmueble> resultado = gestorBusquedas.buscar(
                destino,
                habitaciones,
                banos,
                precioMin,
                precioMax
        );

        model.addAttribute("propiedades", resultado);

        if (gestorUsuarios.esPropietario(username)) {
            return "homePropietario";
        } else if (gestorUsuarios.esInquilino(username)) {
            return "homeInquilino";
        } else {
            return "home";
        }
    }
}


