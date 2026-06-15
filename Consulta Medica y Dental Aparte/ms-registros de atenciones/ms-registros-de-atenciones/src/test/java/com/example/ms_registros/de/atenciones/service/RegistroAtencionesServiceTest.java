package com.example.ms_registros.de.atenciones.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_registros.de.atenciones.client.MedicoClient;
import com.example.ms_registros.de.atenciones.client.PacienteClient;
import com.example.ms_registros.de.atenciones.client.PagosClient;
import com.example.ms_registros.de.atenciones.dto.MedicoResponse;
import com.example.ms_registros.de.atenciones.dto.PacienteResponse;
import com.example.ms_registros.de.atenciones.dto.PagosResponse;
import com.example.ms_registros.de.atenciones.dto.RegistroAtencionesDTO;
import com.example.ms_registros.de.atenciones.dto.RegistroAtencionesResponse;
import com.example.ms_registros.de.atenciones.model.RegistroAtenciones;
import com.example.ms_registros.de.atenciones.repository.RegistroAtencionesRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RegistroAtencionesServiceTest {

    @Mock
    private RegistroAtencionesRepository repository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @Mock
    private PagosClient pagosClient;

    @InjectMocks
    private RegistroAtencionesService service;

    private final String token = "Bearer token-de-prueba";

    private RegistroAtencionesDTO buildDTO() {
        RegistroAtencionesDTO dto = new RegistroAtencionesDTO();
        dto.setNompaciente("Juan Pérez");
        dto.setRunpaciente("11111111-1");
        dto.setNommedico("Dra. Soto");
        dto.setRunmedico("22222222-2");
        dto.setTotal(45000.0);
        dto.setIdPago(1);
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHora(LocalTime.of(10, 30));
        dto.setTratamientoRealizado("Consulta dental");
        return dto;
    }

    private RegistroAtenciones buildRegistro() {
        return new RegistroAtenciones(
                1L,
                "Juan Pérez", "11111111-1",
                "Dra. Soto", "22222222-2",
                45000.0, 1,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 30),
                "Consulta dental"
        );
    }

    private PacienteResponse buildPaciente() {
        PacienteResponse p = new PacienteResponse();
        p.setRunPaciente("11111111-1");
        p.setNombrePaciente("Juan Pérez");
        return p;
    }

    private MedicoResponse buildMedico() {
        MedicoResponse m = new MedicoResponse();
        m.setRunMedico("22222222-2");
        m.setNombreMedico("Dra. Soto");
        return m;
    }

    private PagosResponse buildPago() {
        PagosResponse pago = new PagosResponse();
        pago.setId(1L);
        pago.setTotal(45000.0);
        pago.setEstado("PAGADO");
        return pago;
    }

    @Test
    void deberiaCrearRegistroDeAtencionCorrectamente() {
        RegistroAtencionesDTO dto = buildDTO();
        RegistroAtenciones guardado = buildRegistro();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());
        when(repository.save(any(RegistroAtenciones.class))).thenReturn(guardado);

        // mapToResponse también llama a los clients
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());

        RegistroAtencionesResponse resultado = service.crear(dto, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Pérez", resultado.getPaciente().getNombrePaciente());
        assertEquals("Dra. Soto", resultado.getMedico().getNombreMedico());
        assertEquals("PAGADO", resultado.getPago().getEstado());
        assertEquals("Consulta dental", resultado.getTratamientoRealizado());
        verify(repository).save(any(RegistroAtenciones.class));
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiPacienteNoExiste() {
        RegistroAtencionesDTO dto = buildDTO();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("el paciente no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiMedicoNoExiste() {
        RegistroAtencionesDTO dto = buildDTO();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("El médico no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaLanzarExcepcionAlCrearSiPagoNoExiste() {
        RegistroAtencionesDTO dto = buildDTO();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(null);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.crear(dto, token)
        );

        assertEquals("El pago no existe", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaRetornarListaDeRegistros() {
        when(repository.findAll()).thenReturn(List.of(buildRegistro()));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());

        List<RegistroAtencionesResponse> resultado = service.listar(token);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Juan Pérez", resultado.get(0).getPaciente().getNombrePaciente());
        verify(repository).findAll();
    }

    @Test
    void deberiaRetornarRegistroCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(buildRegistro()));
        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());

        RegistroAtencionesResponse resultado = service.obtener(1L, token);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Consulta dental", resultado.getTratamientoRealizado());
        verify(repository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoRegistroNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.obtener(99L, token)
        );

        assertEquals("Registro de atenciones no se encontro", ex.getMessage());
        verify(repository).findById(99L);
    }

    @Test
    void deberiaActualizarRegistroCorrectamente() {
        RegistroAtencionesDTO dto = buildDTO();
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHora(LocalTime.of(11, 0));
        dto.setTratamientoRealizado("Control");

        RegistroAtenciones existente = buildRegistro();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(RegistroAtenciones.class))).thenAnswer(inv -> inv.getArgument(0));

        RegistroAtencionesResponse resultado = service.actualizar(1L, dto, token);

        assertEquals(LocalDate.of(2026, 6, 25), resultado.getFecha());
        assertEquals(LocalTime.of(11, 0), resultado.getHora());
        assertEquals("Control", resultado.getTratamientoRealizado());
        verify(repository).findById(1L);
        verify(repository).save(existente);
    }

    @Test
    void deberiaLanzarExcepcionAlActualizarSiRegistroNoExiste() {
        RegistroAtencionesDTO dto = buildDTO();

        when(pacienteClient.getPacienteClient("11111111-1", token)).thenReturn(buildPaciente());
        when(medicoClient.getMedicoClient("22222222-2", token)).thenReturn(buildMedico());
        when(pagosClient.getPagosClient(1, token)).thenReturn(buildPago());
        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> service.actualizar(99L, dto, token)
        );

        assertEquals("Registro de atenciones no encontrado", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deberiaEliminarRegistroPorId() {
        doNothing().when(repository).deleteById(1L);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}