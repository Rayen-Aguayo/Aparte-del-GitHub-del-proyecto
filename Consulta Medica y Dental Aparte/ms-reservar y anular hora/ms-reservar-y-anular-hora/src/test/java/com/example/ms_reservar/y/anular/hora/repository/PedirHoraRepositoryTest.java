package com.example.ms_reservar.y.anular.hora.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.glassfish.jaxb.runtime.v2.schemagen.xmlschema.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.ms_reservar.y.anular.hora.model.PedirHora;

@DataJpaTest
@ActiveProfiles("test")
public class PedirHoraRepositoryTest {

    @Autowired
    private PedirHoraRepository repository;

    @Test
    void debeGuardarReserva() {
        PedirHora reserva = new PedirHora(
            null, "11111111-1", 
            "Juan Pérez",
            "22222222-2", "Dra. Soto",
            LocalDate.of(2026, 6, 20), 
            LocalTime.of(10, 30), 
            "Consulta dental"
        );

        PedirHora guardado = repository.save(reserva);

        assertNotNull(guardado.getId());
        assertEquals("Juan Pérez", guardado.getNombrePaciente());
        assertEquals("Dra. Soto", guardado.getNombreMedico());
        assertEquals("Consulta dental", guardado.getAtencion());
    }

    @Test
    void debeBuscarReservaPorId() {
        PedirHora reserva = new PedirHora(
            null, "33333333-3",
            "María López", "44444444-4", "Dr. Rojas",
            LocalDate.of(2026, 7, 1),
            LocalTime.of(9, 0),
            "Control dental"
        );

        PedirHora guardado = repository.save(reserva);

        Optional<PedirHora> resultado = repository.findById(guardado.getId());

        assertTrue(resultado.isPresent());
        assertEquals("María López", resultado.get().getNombrePaciente());
        assertEquals("Dra. Rojas", resultado.get().getNombreMedico());
    }

    @Test 
    void debeListarReservas() {
        repository.save(new PedirHora(
            null, "55555555-5", 
            "Pedro Díaz","66666666-6", 
            "Dra. Fuentes",
            LocalDate.of(2026, 6, 22), 
            LocalTime.of(11, 0), 
            "Urgencia dental")
        );
        
        repository.save(new PedirHora(
            null, "77777777-7", 
            "Ana Torres","88888888-8", 
            "Dr. Muñoz",
            LocalDate.of(2026, 6, 23), 
            LocalTime.of(15, 30), 
            "Consulta médica")
        );
 
        List<PedirHora> resultado = repository.findAll();
 
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.size() >= 2);
    }
 
    @Test
    void debeEliminarReserva() {
        PedirHora reserva = new PedirHora(
            null, "99999999-9", 
            "Carlos Vidal","10101010-1", 
            "Dra. Castro",
            LocalDate.of(2026, 8, 5), 
            LocalTime.of(16, 0), "Control médico"
        );

        PedirHora guardado = repository.save(reserva);
 
        repository.deleteById(guardado.getId());
 
        Optional<PedirHora> resultado = repository.findById(guardado.getId());
        assertFalse(resultado.isPresent());
    }
    
}
