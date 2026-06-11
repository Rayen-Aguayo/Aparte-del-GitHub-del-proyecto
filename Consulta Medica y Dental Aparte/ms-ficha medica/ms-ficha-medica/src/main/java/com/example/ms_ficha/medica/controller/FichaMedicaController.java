package com.example.ms_ficha.medica.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_ficha.medica.dto.ApiResponse;
import com.example.ms_ficha.medica.dto.FichaMedicaDTO;
import com.example.ms_ficha.medica.dto.FichaMedicaResponse;
import com.example.ms_ficha.medica.service.FichaMedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/fichas_medicas")
@RequiredArgsConstructor
public class FichaMedicaController {
    private final FichaMedicaService fichaMedicaService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> crear(@Valid @RequestBody FichaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .message("Ficha médica creada")
                        .data(fichaMedicaService.crear(dto, token))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<FichaMedicaResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<FichaMedicaResponse>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(fichaMedicaService.listar(token))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> obtener(@PathVariable Long id, 
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .data(fichaMedicaService.obtener(id, token))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<FichaMedicaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FichaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<FichaMedicaResponse>builder()
                        .success(true)
                        .message("Se cambió la hora")
                        .data(fichaMedicaService.actualizar(id, dto, token))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        fichaMedicaService.eliminar(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se anuló la hora")
                        .build()
        );
    }
}