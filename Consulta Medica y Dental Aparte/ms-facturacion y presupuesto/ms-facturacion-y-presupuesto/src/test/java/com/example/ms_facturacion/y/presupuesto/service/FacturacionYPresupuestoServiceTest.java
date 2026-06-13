package com.example.ms_facturacion.y.presupuesto.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_facturacion.y.presupuesto.dto.FacturacionYPresupuestoResponse;
import com.example.ms_facturacion.y.presupuesto.model.FacturacionYPresupuesto;
import com.example.ms_facturacion.y.presupuesto.repository.FacturacionYPresupuestoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturacionYPresupuestoServiceTest {

    @Mock
    private FacturacionYPresupuestoRepository repo;

    @InjectMocks
    private FacturacionYPresupuestoService service;

    @Test
    void deberiaRetornarFacturacionYPresupuestoExiste() {
    // Arrange
    FacturacionYPresupuesto facYpre = new FacturacionYPresupuesto(
        1L, 30.000, "paciente","1-1", "medico","1-2","tratamiento",
    8, "gestionPagos");
    when(repo.findById(1L)).thenReturn(Optional.of(facYpre));
    
    String tokenDePrueba = "Bearer token-prueba";

    // Act
    FacturacionYPresupuestoResponse resultado = service.obtener(1L, tokenDePrueba);

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
}
