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

import jakarta.servlet.http.HttpSession;
@Controller
public class VentanaLogin {

    private static final Logger log = LoggerFactory.getLogger(VentanaLogin.class);

    @Autowired
    private GestorUsuarios gestorUsuarios; 

    @GetMapping("/login")
    public String mostrarLogin() {
        log.info("Mostrando formulario de login");
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,  @RequestParam String password, Model model, HttpSession session) {
        String rol_usuario = gestorUsuarios.login(username, password);
        if (rol_usuario == null) {
            log.warn("Login fallido para usuario: {}", username);
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }

        log.info("Login exitoso para usuario: {}", username);
        session.setAttribute("username", username);
        session.setAttribute("rol", rol_usuario);
        return "redirect:/home";
    }

}
