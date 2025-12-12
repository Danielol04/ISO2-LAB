package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.Inquilino;
import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;
import com.nnm.nnm.persistencia.ListaDeseosDAO;

@Service
public class GestorListaDeseos {

    @Autowired
    private GestorInmuebles gestorInmuebles;
    @Autowired
    private GestorUsuarios gestorUsuarios;
    @Autowired
    private ListaDeseosDAO listadeseosDAO;
    
    public boolean toggleInmueble(Long idInmueble, String usernameInquilino) {

        Inquilino inquilino = gestorUsuarios.obtenerInquilinoPorUsername(usernameInquilino);
        ListaDeseos lista = inquilino.getListaDeseos();
        Inmueble inmueble = gestorInmuebles.obtenerInmueblePorId(idInmueble);

        if (inmueble == null) {
            throw new RuntimeException("El inmueble no existe");
        }

        // Crear lista si no existe
        if (lista == null) {
            lista = crearListaDeseos(inquilino);
        }

        // Si ya esta lo quitamos
        if (lista.getInmuebles().contains(inmueble)) {
            quitarInmueble(lista, inmueble);  
            return false;
        }

        // Si no está lo añadimos
        añadirInmueble(lista, inmueble);
        return true;
    }

    private void añadirInmueble(ListaDeseos lista, Inmueble inmueble) {

        if (!lista.getInmuebles().contains(inmueble)) {
            lista.getInmuebles().add(inmueble);
            listadeseosDAO.save(lista);
        }
    }
    
    private void quitarInmueble(ListaDeseos lista, Inmueble inmueble) {

        if (lista.getInmuebles().contains(inmueble)) {
            lista.getInmuebles().remove(inmueble);
            listadeseosDAO.save(lista);
        }
    }

    private ListaDeseos crearListaDeseos(Inquilino inquilino){
        ListaDeseos lista = new ListaDeseos();
        lista.setInquilino(inquilino);
        inquilino.setListaDeseos(lista);
        return lista;
    }


}