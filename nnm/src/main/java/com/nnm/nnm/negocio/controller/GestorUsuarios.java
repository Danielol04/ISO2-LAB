package com.nnm.nnm.negocio.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nnm.nnm.negocio.dominio.entidades.Usuario;
import com.nnm.nnm.persistencia.UsuarioDAO;


@Controller
public class GestorUsuarios {
    public static final Logger log = LoggerFactory.getLogger(GestorUsuarios.class);

    @Autowired
    private UsuarioDAO usuarioDAO;
    
    @GetMapping("/usuarios")
    public String usuariosForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        log.info(usuarioDAO.findAll().toString());
        return "usuario";
    }

    @PostMapping("/usuarios")
    public String usuariosSubmit(@ModelAttribute Usuario usuario, Model model) {
        model.addAttribute("usuario", usuario);
        Usuario savedUser = usuarioDAO.save(usuario);
        log.info("Usuario guardado: " + savedUser);
        return "result";
    }
}