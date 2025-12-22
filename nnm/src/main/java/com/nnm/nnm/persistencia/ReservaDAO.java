package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Reserva;

@Repository
public class ReservaDAO extends EntidadDAO<Reserva, Long> {

    public ReservaDAO() {
        super(Reserva.class);
    }

    public List<Reserva> findReservasByInmuebleId(long inmuebleId) {
        String jpql = "SELECT r FROM Reserva r WHERE r.inmueble.id = :inmuebleId";
        return gestorBD.selectList(jpql, Reserva.class, "inmuebleId", inmuebleId);
    }

    public List<Reserva> findReservasByInquilinoUsername(String username) {
        String jpql = "SELECT r FROM Reserva r WHERE r.inquilino.username = :username";
        return gestorBD.selectList(jpql, Reserva.class, "username", username);
    }

    public List<Reserva> findReservasByPropietarioUsername(String username) {
        String jpql = "SELECT r FROM Reserva r WHERE r.inmueble.propietario.username = :username";
        return gestorBD.selectList(jpql, Reserva.class, "username", username);
    }
}