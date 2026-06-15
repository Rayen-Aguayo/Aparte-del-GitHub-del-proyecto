package com.example.ms_ficha.medica.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_ficha.medica.client.MedicoClient;
import com.example.ms_ficha.medica.client.PacienteClient;
import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.dto.MedicoResponse;
import com.example.ms_ficha.medica.dto.PacienteResponse;
import com.example.ms_ficha.medica.model.FichaMedica;
import com.example.ms_ficha.medica.repository.FichaMedicaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class FichaMedicaServiceTest {

    @Mock
    private FichaMedicaRepository repo;

    @InjectMocks
    private FichaMedicaService service;

    @Mock
    private MedicoClient medicoClient;
    
    @Mock
    private PacienteClient pacienteClient;

    @Test
    void deberiaRetornarFichaMedicaExiste() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica fichaMedica = new FichaMedica(
        1L, 30.000, "paciente","1-1",
         "medico","1-2","tratamiento",
    8, "gestionPagos");
    when(repo.findById(1L)).thenReturn(Optional.of(fichaMedica));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    FichaMedicaResponse resultado = service.obtener(1L, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("1-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).findById(1L);
}
@Test
void deberiaLanzarExcepcionCuandoFacturacionYPresupuestoNoExiste() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.obtener(99L, tokenDePrueba)
    );

    assertEquals("Facturacion y presupuesto encontrado", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaRetornarListaFichaMedica() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica fichaMedica = new FichaMedica( 1L, 30.000, "paciente","1-1",
     "medico","1-2","tratamiento",
    8, "gestionPagos");

    when(repo.findAll()).thenReturn(List.of(fichaMedica));

    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    // Act
    List<FichaMedicaResponse> resultado = service.listar(null);

    // Assert
    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());

    FichaMedicaResponse item = resultado.get(0);

    assertEquals(30.000, item.getPresupuesto());

    assertNotNull(item.getPaciente());
    assertEquals("paciente", item.getPaciente().getNombrePaciente());
    assertEquals("1-1", item.getPaciente().getRunPaciente());

    assertNotNull(item.getMedico());
    assertEquals("medico", item.getMedico().getNombreMedico());
    assertEquals("1-2", item.getMedico().getRunMedico());

    assertEquals("tratamiento", item.getTratamiento());
    assertEquals(8, item.getDiasDuracion());
    assertEquals("gestionPagos", item.getGestionPagos());

    verify(repo).findAll();
}
@Test
void deberiaCrearFichaMedicaCorrectamente() {
    
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedicaDTO dto = new FichaMedicaDTO();
                    
                    dto.setPresupuesto(30.000);
                    dto.setNombrePaciente("paciente");    
                    dto.setRunPaciente("1-1");
                    dto.setNombreMedico("medico");
                    dto.setRunMedico("1-2");
                    dto.setTratamiento("tratamiento");
                    dto.setDiasDuracion(8);
                    dto.setGestionPagos("gestionPagos");


    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);

    FichaMedica Guardado = new FichaMedica(1L, 30.000, "paciente","1-1",
     "medico","1-2","tratamiento",
    8, "gestionPagos");
    when(repo.save(any(FichaMedica.class))).thenReturn(Guardado);

    // Act
    FichaMedicaResponse resultado = service.crear(dto,tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("1-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).save(any(FichaMedica.class));
}

@Test
void deberiaActualizarFichaMedicaCorrectamente() {
    // Arrange
    String tokenDePrueba = "Bearer token-prueba";

    FichaMedica existente = new FichaMedica(1L, 30.000, "paciente","1-1",
     "medico","1-2","tratamiento",
    8, "gestionPagos");
    
    PacienteResponse pacienteResponse = new PacienteResponse();
    pacienteResponse.setRunPaciente("1-1");
    pacienteResponse.setNombrePaciente("paciente");
    
    when(pacienteClient.getPacienteClient("1-1", tokenDePrueba)).thenReturn(pacienteResponse);

    MedicoResponse medicoResponse = new MedicoResponse();
    medicoResponse.setRunMedico("1-2");
    medicoResponse.setNombreMedico("medico");

    when(medicoClient.getMedicoClient("1-2", tokenDePrueba)).thenReturn(medicoResponse);


    FichaMedicaDTO dto = new FichaMedicaDTO();
                    dto.setPresupuesto(30.000);
                    dto.setNombrePaciente("paciente");    
                    dto.setRunPaciente("1-1");
                    dto.setNombreMedico("medico");
                    dto.setRunMedico("1-2");
                    dto.setTratamiento("tratamiento");
                    dto.setDiasDuracion(8);
                    dto.setGestionPagos("gestionPagos");


    when(repo.findById(1L)).thenReturn(Optional.of(existente));
    when(repo.save(any(FichaMedica.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    FichaMedicaResponse resultado = service.actualizar(1L, dto, tokenDePrueba);

    // Assert
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(30.000, resultado.getPresupuesto());

    assertNotNull(resultado.getPaciente());
    assertEquals("paciente", resultado.getPaciente().getNombrePaciente());
    assertEquals("1-1", resultado.getPaciente().getRunPaciente());

    assertNotNull(resultado.getMedico());
    assertEquals("medico", resultado.getMedico().getNombreMedico());
    assertEquals("1-2", resultado.getMedico().getRunMedico());

    assertEquals("tratamiento", resultado.getTratamiento());
    assertEquals(8, resultado.getDiasDuracion());
    assertEquals("gestionPagos", resultado.getGestionPagos());

    verify(repo).findById(1L);
    verify(repo).save(existente);
}
@Test
void deberiaLanzarExcepcionCuandoFichaMedicaNoSeActualizoCorectamente() {
    // Arrange
    when(repo.findById(99L)).thenReturn(Optional.empty());

    // Act + Assert
    String tokenDePrueba = "Bearer token-prueba";

    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.actualizar(99L,null, tokenDePrueba)
    );

    assertEquals("Facturacion y presupuesto encontrado", ex.getMessage());
    verify(repo).findById(99L);
}

@Test
void deberiaEliminarFichaMedicaPorId() {
    // Arrange
    
    doNothing().when(repo).deleteById(1L);

    // Act
    service.eliminar(1L);

    // Assert
    verify(repo).deleteById(1L);
}

@Test
void deberiaLanzarExcepcionCuandoFichaMedicaNoSeEliminoCorectamente() {
    // Arrange
    when(repo.existsById(99L)).thenReturn(false); 

    // Act + Assert
    EntityNotFoundException ex = assertThrows(
            EntityNotFoundException.class,
            () -> service.eliminar(99L)
    );

    assertEquals("FacturacionYPresupuesto no encontrado", ex.getMessage());
    verify(repo).existsById(99L);
    verify(repo, never()).deleteById(99L); 
}


}
