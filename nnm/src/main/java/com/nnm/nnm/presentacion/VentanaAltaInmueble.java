package com.nnm.nnm.presentacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nnm.nnm.negocio.controller.GestorInmuebles;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;

@Controller
@RequestMapping("/Inmueble")
public class VentanaAltaInmueble{
    
    private static final Logger log = LoggerFactory.getLogger(VentanaRegistro.class);

    @Autowired
    private GestorInmuebles gestorInmuebles;

    //mostramos el formulario de alta
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){
        log.info("Mostrando formulario de AltaInmueble");
        model.addAttribute("inmueble", new Inmueble());
        return "AltaInmuebles";  
    }

    //procesamos el formulario de alta
    public String procesarAlta(@ModelAttribute("inmueble") Inmueble inmueble, Model model ){
        try{
            if(inmueble.getDireccion() == null ||inmueble.getDireccion().isEmpty() ){
                model.addAttribute("Error", "La direccion del Inmueble es obligatoria");
                return "AltaInmuebles";
            }

            gestorInmuebles.registrarInmueble(inmueble);
            log.info("Mostrando formulario de AltaInmueble");
            model.addAttribute("mensaje", "Inmueble registrado correctamente");
            return "resultadoAltaInmueble"; //confirmacion
        } catch (Exception e){
            model.addAttribute("error", "error al registrar el inmueble:" + e.getMessage());
            return "AltaInmuebles";
        }
      

   
    
    }   

}
