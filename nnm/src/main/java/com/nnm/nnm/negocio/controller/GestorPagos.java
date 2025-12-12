package com.nnm.nnm.negocio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Pago;
import com.nnm.nnm.persistencia.PagoDAO;

@Service
public class GestorPagos {
    @Autowired
    private PagoDAO pagoDAO;
    public void registrarPago(com.nnm.nnm.negocio.dominio.entidades.Pago pago) {
        pagoDAO.guardarPago(pago);
    }

    public Pago obtenerPagoPorReserva(Long idReserva) {
        return pagoDAO.findByReservaID(idReserva);
    }

    public void borrarPago(Pago pago) {
        pagoDAO.delete(pago);
    }
}