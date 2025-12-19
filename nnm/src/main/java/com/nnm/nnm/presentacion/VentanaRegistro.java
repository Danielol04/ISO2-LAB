package com.nnm.nnm.presentacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.Propietario;

@Controller
public class VentanaRegistro {
    private  static final Logger log = LoggerFactory.getLogger(VentanaRegistro.class);
    private static final String REGISTRO = "registro";
    
    private final GestorUsuarios gestorUsuarios; 

    @Autowired
    public VentanaRegistro(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        log.info("Mostrando formulario de registro");
        return REGISTRO;
    }
    
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String userType, @RequestParam String username, @RequestParam String password,
                                   @RequestParam String correo, @RequestParam String nombre, @RequestParam String apellidos,
                                   @RequestParam String direccion, Model model) {

        if (gestorUsuarios.existeUsuario(username)) {
            model.addAttribute("error", "El nombre de usuario ya existe");
            return REGISTRO;
        }

        if ("PROPIETARIO".equalsIgnoreCase(userType)) {
            Propietario propietario = new Propietario(username, password, correo, nombre, apellidos, direccion);
            gestorUsuarios.registrarPropietario(propietario);

        } else if ("INQUILINO".equalsIgnoreCase(userType)) {
            Inquilino inquilino = new Inquilino(username, password, correo, nombre, apellidos, direccion);
            gestorUsuarios.registrarInquilino(inquilino);

        } else {
            model.addAttribute("error", "Tipo de usuario no válido");
            return REGISTRO;
        }
        log.info("Usuario {} registrado", username.replaceAll("[\n\r]", "_"));
        model.addAttribute("mensaje", "Usuario registrado correctamente");
        return "login";
    }
    
}
