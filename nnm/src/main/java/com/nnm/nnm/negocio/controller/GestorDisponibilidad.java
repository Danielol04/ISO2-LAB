package com.nnm.nnm.negocio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
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
            nueva.getFechaFin()
        );
        if(adyacentes.isEmpty()){
            disponibilidadDAO.save(nueva);
        }else{
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
                nueva.getReservaDirecta()
            );

            disponibilidadDAO.save(fusionada);
            
        }

    }

    public void actualizarDisponibilidadPorReserva(long idInmueble, LocalDate reservaInicio, LocalDate reservaFin) {
        // Buscar disponibilidades que se solapen con la reserva
        Disponibilidad d = disponibilidadDAO.findDisponibilidadparaReserva(idInmueble, reservaInicio, reservaFin);
        // Borrar la disponibilidad original
        disponibilidadDAO.delete(d);
    
        // Crear disponibilidad antes de la reserva
        if (reservaInicio.isAfter(d.getFechaInicio())) {
            Disponibilidad antes = new Disponibilidad();
            antes.setInmueble(d.getInmueble());
            antes.setFechaInicio(d.getFechaInicio());
            antes.setFechaFin(reservaInicio.minusDays(1));
            antes.setReservaDirecta(d.getReservaDirecta());
            antes.setPoliticaCancelacion(d.getPoliticaCancelacion());
            disponibilidadDAO.save(antes);
        }
    
        // Crear disponibilidad después de la reserva
        if (reservaFin.isBefore(d.getFechaFin())) {
            Disponibilidad despues = new Disponibilidad();
            despues.setInmueble(d.getInmueble());
            despues.setFechaInicio(reservaFin.plusDays(1));
            despues.setFechaFin(d.getFechaFin());
            despues.setReservaDirecta(d.getReservaDirecta());
            despues.setPoliticaCancelacion(d.getPoliticaCancelacion());
            disponibilidadDAO.save(despues);
        }
    }
    
    

    public List<Disponibilidad> obtenerDisponibilidadPorInmueble(long id_inmueble) {
        return disponibilidadDAO.findByInmueble(id_inmueble);
    }


}
