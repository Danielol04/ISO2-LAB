package com.nnm.nnm.persistencia;
import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Pago;
@Repository
public class PagoDAO extends EntidadDAO<Pago, Long > {
    public PagoDAO() {
        super(Pago.class);
    }

    public void guardarPago(Pago pago) {
        save(pago);
    }

    public Pago findByReservaID(Long idReserva) {
        String jpql = "SELECT p FROM Pago p WHERE p.idReserva = :idReserva";
        return gestorBD.selectSingle(jpql, Pago.class, "idReserva", idReserva);
    }
}