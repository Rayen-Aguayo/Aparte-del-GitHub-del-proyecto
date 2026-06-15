package com.example.ms_medico.service;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.repository.MedicoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @Mock
    private MedicoRepository repo;

    @InjectMocks
    private MedicoService service;

    @Test
    void deberiaRetornarMedicoCuandoExiste() {
        Medico medico = new Medico("1-2","nombre", 28, "123456789",
            "especialidad","firmaMedico"
        );

        when(repo.findById("1-2")).thenReturn(Optional.of(medico));

        Medico resultado = service.obtener("1-2");

        assertNotNull(resultado);
        assertEquals("1-2", resultado.getRunMedico());
        assertEquals("nombre", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());

        verify(repo).findById("1-2");
    }

    @Test
    void deberiaLanzarExcepcionCuandoMedicoNoExiste() {
        when(repo.findById("99")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener("99")
        );

        assertEquals("Medico no encontrado", ex.getMessage());
        verify(repo).findById("99");
    }

    @Test
    void deberiaRetornarListaMedicos() {
        Medico medico = new Medico("1-2","nombre", 28, "123456789",
            "especialidad","firmaMedico"
        );
        when(repo.findAll()).thenReturn(List.of(medico));

        List<Medico> resultado = service.listar();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("nombre", resultado.get(0).getNombreMedico());
        verify(repo).findAll();
    }

    @Test
    void deberiaCrearMedicoCorrectamente() {
        MedicoDTO dto = new MedicoDTO();
        dto.getNombreMedico();
        dto.getEspecialidad();
        dto.getEdad();
        dto.getNroTelefono();
        dto.getFirmaMedico();

        Medico Guardado = new Medico("1-2","nombre", 28, "123456789",
            "especialidad","firmaMedico"
        );
        when(repo.save(any(Medico.class))).thenReturn(Guardado);

        Medico resultado = service.crear(dto);

        assertNotNull(resultado);
        assertEquals("1-2", resultado.getRunMedico());
        assertEquals("nombre", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());
        verify(repo).save(any(Medico.class));
    }

    @Test
    void deberiaActualizarMedicoCorrectamente() {
        Medico existente = new Medico("1-2","nombre", 28, "123456789",
            "especialidad","firmaMedico"
        );

        MedicoDTO dto = new MedicoDTO();
        dto.getNombreMedico();
        dto.getEspecialidad();
        dto.getEdad();
        dto.getNroTelefono();
        dto.getFirmaMedico();

        when(repo.findById("1-2")).thenReturn(Optional.of(existente));
        when(repo.save(any(Medico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medico resultado = service.actualizar("1-2", dto);

        assertEquals("1-2", resultado.getRunMedico());
        assertEquals("nombre", resultado.getNombreMedico());
        assertEquals(28, resultado.getEdad());
        assertEquals("123456789", resultado.getNroTelefono());
        assertEquals("especialidad", resultado.getEspecialidad());
        assertEquals("firmaMedico", resultado.getFirmaMedico());
        verify(repo).findById("1-2");
        verify(repo).save(existente);
    }

    @Test
    void deberiaEliminarMedicoPorId() {
        doNothing().when(repo).deleteById("1-2");

        service.eliminar("1-2");

        verify(repo).deleteById("1-2");
    }
}

