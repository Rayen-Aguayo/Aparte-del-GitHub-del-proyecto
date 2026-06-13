package com.example.ms_opinion.del.paciente.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_opinion.del.paciente.dto.ApiResponse;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteDTO;
import com.example.ms_opinion.del.paciente.dto.OpinionPacienteResponse;
import com.example.ms_opinion.del.paciente.service.OpinionPacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Opinión del Paciente", description = "Operaciones relacionadas con las opiniones y valoraciones de los pacientes sobre su experiencia en la consulta médica y dental")
@RestController
@RequestMapping("/api/v1/opiniones")
@RequiredArgsConstructor
public class OpinionPacienteController {
    private final OpinionPacienteService opinionPacienteService;
    
    @Operation(
        summary = "Crear una nueva opinión del paciente",
        description = "Permite registrar una nueva opinión del paciente sobre su experiencia en la consulta médica y dental. Requiere rol USER o ADMIN.")
        
        @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Opinión creada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado o token inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OpinionPacienteResponse>> crear(@Valid @RequestBody OpinionPacienteDTO dto,@RequestHeader("Authorization") String token) {
        OpinionPacienteResponse response = opinionPacienteService.crear(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.<OpinionPacienteResponse>builder()
                .success(true)
                .message("Opinión registrada exitosamente")
                .data(response)
                .build()
        );
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OpinionPacienteResponse>>> listar(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
            ApiResponse.<List<OpinionPacienteResponse>>builder()
                .success(true)
                .message("Listado de opiniones obtenido")
                .data(opinionPacienteService.listar(token))
                .build()
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<OpinionPacienteResponse>> obtener(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
            ApiResponse.<OpinionPacienteResponse>builder()
                .success(true)
                .message("Detalle de la opinión obtenido")
                .data(opinionPacienteService.obtener(id,token))
                .build()
        );
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        opinionPacienteService.eliminar(id);
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .success(true)
                .message("La opinión ha sido eliminada")
                .build()
        );
    }
}