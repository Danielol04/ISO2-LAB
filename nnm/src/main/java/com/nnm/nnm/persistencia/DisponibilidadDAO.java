package com.nnm.nnm.persistencia;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;

@Repository
public class DisponibilidadDAO extends EntidadDAO<Disponibilidad, Long> {

    public DisponibilidadDAO() {
        super(Disponibilidad.class);
    }

    public List<Disponibilidad> findByInmueble(long id_inmueble) {
        String jpql = "FROM Disponibilidad d WHERE d.inmueble.id = :id_inmueble";
        return gestorBD.selectList(jpql, Disponibilidad.class, "id_inmueble", id_inmueble);
    }
}
