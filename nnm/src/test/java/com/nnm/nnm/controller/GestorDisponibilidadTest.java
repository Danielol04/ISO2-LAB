package com.nnm.nnm.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.nnm.nnm.negocio.controller.GestorDisponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.Disponibilidad;
import com.nnm.nnm.negocio.dominio.entidades.PoliticaCancelacion;

class GestorDisponibilidadTest {

    // Como GestorDisponibilidad tiene métodos privados de lógica, 
    // podemos testear el cálculo de política directamente
    
    @Test
    void testCalcularPoliticaMasRestrictiva() {
        GestorDisponibilidad gestor = new GestorDisponibilidad(null); // DAO no necesario para este método
        
        Disponibilidad d1 = new Disponibilidad();
        d1.setPoliticaCancelacion(PoliticaCancelacion.REEMBOLSABLE);
        
        Disponibilidad d2 = new Disponibilidad();
        d2.setPoliticaCancelacion(PoliticaCancelacion.NO_REEMBOLSABLE);
        
        PoliticaCancelacion resultado = gestor.calcularPoliticaCancelacion(List.of(d1, d2));
        
        assertEquals(PoliticaCancelacion.NO_REEMBOLSABLE, resultado);
    }
}