package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.ListaDeseos;

@Repository
public class ListaDeseosDAO extends EntidadDAO<ListaDeseos, Long> {
   
    public ListaDeseosDAO() {
        super(ListaDeseos.class);
    }

    
}