package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorInmuebles{

    @Autowired
    private InmuebleDAO inmuebleDAO;

    public boolean autenticarInmueble(long Id, String username_propietario ){
        Inmueble inmueble = inmuebleDAO.findById(Id);
        return (inmueble != null && inmueble.getUsername_propietario().equals(username_propietario));

    }

    //comprobamos que el Inmueble existe en nuestra base de datos
    public boolean autenticarInmueble(long Id){
        return inmuebleDAO.findById(Id) != null; //Si el inmueble ya existe devolvera true, si no nos dara False

    }

    public void registrarInmueble(Inmueble inmueble){
        inmuebleDAO.save(inmueble);
    }
}