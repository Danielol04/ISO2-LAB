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

     private static final String USERNAME = "username";
    private final GestorInmuebles gestorInmuebles;
    private final GestorBusquedas gestorBusquedas;
    private final GestorUsuarios gestorUsuarios;

    @Autowired
    public VentanaHome(GestorInmuebles gestorInmuebles, GestorBusquedas gestorBusquedas, GestorUsuarios gestorUsuarios) {
        this.gestorInmuebles = gestorInmuebles;
        this.gestorBusquedas = gestorBusquedas;
        this.gestorUsuarios = gestorUsuarios;
    }  

        @GetMapping("/home")
        public String mostrarHome(Model model, HttpSession session) {
            String username = (String) session.getAttribute(USERNAME);
            List<Inmueble> propiedades;
            if (gestorUsuarios.esPropietario(username)) {
                log.info("Redirigiendo al propietario a su página de inicio");
                propiedades = gestorInmuebles.listarInmueblesPorPropietario(username);
                model.addAttribute("propiedades", propiedades);
                model.addAttribute(USERNAME, username);
                return "homePropietario";

        } else if (gestorUsuarios.esInquilino(username)) {
            log.info("Home inquilino");
            propiedades = gestorInmuebles.listarInmuebles();
            model.addAttribute("propiedades", propiedades);
                model .addAttribute(USERNAME, username);
            return "homeInquilino";

        } else {
            log.info("Home público");
            propiedades = gestorInmuebles.listarInmuebles();
            model.addAttribute("propiedades", propiedades);
            return "home";
        }
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) Integer habitaciones,
            @RequestParam(required = false) Integer banos,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model,
            HttpSession session) {

        String username = (String) session.getAttribute(USERNAME);

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


