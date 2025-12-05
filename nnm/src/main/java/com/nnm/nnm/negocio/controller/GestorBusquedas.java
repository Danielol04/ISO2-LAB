package com.nnm.nnm.negocio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Inmueble;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.persistencia.InmuebleDAO;

@Service
public class GestorBusquedas {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    public List<Inmueble> buscar(
            String localidad,
            String provincia,
            String titulo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String tipo,
            Integer habitaciones,
            Integer banos,
            Boolean reservaDirecta,
            PoliticaCancelacion politica
    ) {
        return inmuebleDAO.buscarFiltrado(
                localidad,
                provincia,
                titulo,
                fechaInicio,
                fechaFin,
                tipo,
                habitaciones,
                banos,
                reservaDirecta,
                politica
        );
    }
}
