package com.nnm.nnm.presentacion;

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

    @Autowired
    private GestorInmuebles gestorInmuebles;

    //mostramos el formulario de alta
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model){
        model.addAttribute("inmueble", new Inmueble());
        return "altaInmueble";  
    }

    //procesamos el formulario de alta
    public String procesarAlta(@ModelAttribute("inmueble") Inmueble inmueble, Model model ){
        try{
            if(inmueble.getDireccion() == null ||inmueble.getDireccion().isEmpty() ){
                model.addAttribute("Error", "La direccion del Inmueble es obligatoria");
                return "altaInmueble";
            }

            gestorInmuebles.registrarInmueble(inmueble);
            model.addAttribute("mensaje", "Inmueble registrado correctamente");
            return "resultadoAltaInmueble"; //confirmacion
        } catch (Exception e){
            model.addAttribute("error", "error al registrar el inmueble:" + e.getMessage());
            return "altaInmueble";
        }
      

   
    
    }   

}
