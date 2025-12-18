package com.nnm.nnm.presentacion;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnm.nnm.negocio.controller.GestorListaDeseos;
import com.nnm.nnm.negocio.controller.GestorUsuarios;
import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;

import jakarta.servlet.http.HttpSession;

@Controller
public class VentanaListaDeseos{

    private final GestorListaDeseos gestorLista;
    private final GestorUsuarios gestorUsuarios;

    @Autowired
    public VentanaListaDeseos(GestorListaDeseos gestorLista, GestorUsuarios gestorUsuarios) {
        this.gestorLista = gestorLista;
        this.gestorUsuarios = gestorUsuarios;
    }

    @PostMapping("/favoritos/toggle")
    @ResponseBody
    public ResponseEntity<Boolean> toggleFavorito(@RequestParam Long idInmueble, HttpSession session) {

        String username = (String) session.getAttribute("username");

        // Usuario no logueado lanzar 401 Unauthorized
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // true = añadido, false = eliminado
        boolean nuevoEstado = gestorLista.toggleInmueble(idInmueble, username);

        return ResponseEntity.ok(nuevoEstado);
    }
   
    @GetMapping("/favoritos/lista")
    @ResponseBody
    public ResponseEntity<Set<Long>> obtenerFavoritos(HttpSession session) {
        String username = (String) session.getAttribute("username");

        // Usuario no logueado -> 401 Unauthorized
        if (username == null) {
            return ResponseEntity.ok(Collections.emptySet());
        }

        Inquilino inquilino = gestorUsuarios.obtenerInquilinoPorUsername(username);

        // Si el usuario no tiene lista de deseos -> devolver Set vacío
        if (inquilino.getListaDeseos() == null) {
            return ResponseEntity.ok(Collections.emptySet());
        }

        // Obtener IDs de los inmuebles en la lista de deseos
        Set<Long> idsFavoritos = inquilino.getListaDeseos().getInmuebles()
                                        .stream()
                                        .map(Inmueble::getId)
                                        .collect(Collectors.toSet());

        return ResponseEntity.ok(idsFavoritos);
    }

    @GetMapping("/lista-deseos")
    public String mostrarListaDeseos(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        Inquilino inquilino = gestorUsuarios.obtenerInquilinoPorUsername(username);

        if (inquilino.getListaDeseos() == null) {
            model.addAttribute("propiedades", Collections.emptyList());
        } else {
            model.addAttribute("propiedades", inquilino.getListaDeseos().getInmuebles());
        }

        return "lista-deseos";
    }


    
}