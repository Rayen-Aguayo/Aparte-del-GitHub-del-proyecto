package com.example.ms_registros.de.atenciones.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.ms_registros.de.atenciones.dto.ApiResponse;
import com.example.ms_registros.de.atenciones.dto.RegistroAtencionesDTO;
import com.example.ms_registros.de.atenciones.dto.RegistroAtencionesResponse;
import com.example.ms_registros.de.atenciones.service.RegistroAtencionesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/registro-atenciones")
@RequiredArgsConstructor
public class RegistroAtencionesController {

    private final RegistroAtencionesService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> crear(
            @Valid @RequestBody RegistroAtencionesDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .message("Registro de atenciones creado")
                        .data(service.crear(dto, token))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<RegistroAtencionesResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<RegistroAtencionesResponse>>builder()
                        .success(true)
                        .data(service.listar(token))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .data(service.obtener(id, token))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroAtencionesResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RegistroAtencionesDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RegistroAtencionesResponse>builder()
                        .success(true)
                        .message("Registro de atenciones actualizado")
                        .data(service.actualizar(id, dto, token))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Registro de atenciones eliminado")
                        .build()
        );
    }
}
