package com.nnm.nnm.persistencia;

import org.springframework.stereotype.Repository;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;

@Repository
public class DisponibilidadDAO extends EntidadDAO<Disponibilidad, Long> {

    public DisponibilidadDAO() {
        super(Disponibilidad.class);
    }
}
