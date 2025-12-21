package com.nnm.nnm.negocio.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.persistencia.PagoDAO;

@Service
public class GestorPagos {

    private final PagoDAO pagoDAO;

    @Autowired
    public GestorPagos(PagoDAO pagoDAO) {
        this.pagoDAO = pagoDAO;
    }

    public void registrarPago(com.nnm.nnm.negocio.dominio.entidades.Pago pago) {
        pagoDAO.guardarPago(pago);
    }
}