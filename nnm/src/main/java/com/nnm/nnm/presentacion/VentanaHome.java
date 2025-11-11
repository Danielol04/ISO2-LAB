package com.nnm.nnm.presentacion;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

import jakarta.servlet.http.HttpSession;



@Controller
public class VentanaHome {
        private static final Logger log = LoggerFactory.getLogger(VentanaLogin.class);
        @Autowired
        private GestorInmuebles gestorInmuebles;
        @Autowired
        private GestorUsuarios gestorUsuarios;

        @GetMapping("/home")
        public String mostrarHome(Model model, HttpSession session) {
            String username = (String) session.getAttribute("username");
            List<Inmueble> propiedades;
            if (gestorUsuarios.esPropietario(username)) {
                log.info("Redirigiendo al propietario a su página de inicio");
                propiedades = gestorInmuebles.listarInmueblesPorPropietario(username); 
                model.addAttribute("propiedades", propiedades);
                return "homePropietario";

            } else if (gestorUsuarios.esInquilino(username)) {
                log.info("Redirigiendo al inquilino a su página de inicio");
                propiedades = gestorInmuebles.listarInmuebles(); 
                model.addAttribute("propiedades", propiedades);
                return "homeInquilino";
            } else {
                log.info("Mostrando la página de inicio generico");
                propiedades = gestorInmuebles.listarInmuebles();
                model.addAttribute("propiedades", propiedades);
                return "home";
            }
        }
}