package com.nnm.nnm.negocio.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;
import com.nnm.nnm.persistencia.DisponibilidadDAO;

@Service
public class GestorDisponibilidad {

    @Autowired
    private DisponibilidadDAO disponibilidadDAO;

    public void registrarDisponibilidad(Disponibilidad nueva) {
        List<Disponibilidad> adyacentes = disponibilidadDAO.encontrarAdyacentes(
                nueva.getInmueble().getId(),
                nueva.getPoliticaCancelacion(),
                nueva.getReservaDirecta(),
                nueva.getFechaInicio(),
                nueva.getFechaFin());
        if (adyacentes.isEmpty()) {
            disponibilidadDAO.save(nueva);
        } else {
            // Hay vecinos, unirlos
            LocalDate minFechaInicio = nueva.getFechaInicio();
            LocalDate maxFechaFin = nueva.getFechaFin();

            for (Disponibilidad d : adyacentes) {
                if (d.getFechaInicio().isBefore(minFechaInicio)) {
                    minFechaInicio = d.getFechaInicio();
                }
                if (d.getFechaFin().isAfter(maxFechaFin)) {
                    maxFechaFin = d.getFechaFin();
                }
                // Eliminar la disponibilidad vecina que vamos a fusionar
                disponibilidadDAO.delete(d);
            }

            // Crear una nueva disponibilidad fusionada
            Disponibilidad fusionada = new Disponibilidad(
                    nueva.getInmueble(),
                    minFechaInicio,
                    maxFechaFin,
                    nueva.getPoliticaCancelacion(),
                    nueva.getReservaDirecta());

            disponibilidadDAO.save(fusionada);

        }

    }

    public List<Disponibilidad> obtenerDisponibilidadPorInmueble(long id_inmueble) {
        return disponibilidadDAO.findByInmueble(id_inmueble);
    }

    public void eliminarDisponibilidad(Disponibilidad disponibilidad) {
        disponibilidadDAO.delete(disponibilidad);
    }

    public Disponibilidad obtenerDisponibilidadPorId(long id) {
        return disponibilidadDAO.findById(id);
    }

    public Boolean obtenerTipoReserva(long idInmueble, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Disponibilidad> disponibles = obtenerDisponibilidadPorInmueble(idInmueble);

        for (Disponibilidad d : disponibles) {
            // Comprobamos si la disponibilidad cubre todo el rango solicitado
            if (!fechaInicio.isBefore(d.getFechaInicio()) && !fechaFin.isAfter(d.getFechaFin())) {
                return d.getReservaDirecta(); // Devuelve true si es directa, false si no
            }
        }
        return null;
    }

    public List<Disponibilidad> obtenerDisponibilidadParaReserva(long idInmueble, LocalDate fechaInicio,
            LocalDate fechaFin) {
        List<Disponibilidad> disponibles = obtenerDisponibilidadPorInmueble(idInmueble);
        List<Disponibilidad> afectadas = new ArrayList<>();

        for (Disponibilidad d : disponibles) {
            // Comprobamos si la disponibilidad cubre todo el rango solicitado
            if (!fechaFin.isBefore(d.getFechaInicio()) && !fechaInicio.isAfter(d.getFechaFin())) {
                afectadas.add(d);
            }
        }
        return afectadas;
    }

    public Boolean calcularTipoReserva(List<Disponibilidad> afectadas) {
        for (Disponibilidad d : afectadas) {
            if (!d.getReservaDirecta()) {
                return false;
            }
        }
        return true;
    }

    public PoliticaCancelacion calcularPoliticaCancelacion(List<Disponibilidad> afectadas) {
        PoliticaCancelacion politicaMasRestrictiva = PoliticaCancelacion.REEMBOLSABLE;
        int peorPolitica=1;
        for (Disponibilidad d : afectadas) {
            int prioridad= prioridadPolitica(d.getPoliticaCancelacion());
            if(prioridad>peorPolitica){
                peorPolitica=prioridad;
                politicaMasRestrictiva=d.getPoliticaCancelacion();
            }
        }
        return politicaMasRestrictiva;
    }

    private int prioridadPolitica(PoliticaCancelacion p) {
        return switch (p) {
            case REEMBOLSABLE -> 1;
            case REEMBOLSABLE_50_PER -> 2;
            case NO_REEMBOLSABLE -> 3; // peor
        };
    }
}
