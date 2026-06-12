package com.example.Receta.Medica.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Receta.Medica.dto.ApiResponse;
import com.example.Receta.Medica.dto.RecetaMedicaDTO;
import com.example.Receta.Medica.dto.RecetaMedicaResponce;
import com.example.Receta.Medica.service.RecetamedicaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recetasMedicas")
@RequiredArgsConstructor
public class RecetaMedicaController {

    private final RecetamedicaService recetamedicaService;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> crear(
            @Valid @RequestBody RecetaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .message("Receta médica creada")
                        .data(recetamedicaService.crear(dto, token))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RecetaMedicaResponce>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<RecetaMedicaResponce>>builder()
                        .success(true)
                        .data(recetamedicaService.listar(token))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .data(recetamedicaService.obtener(id, token))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RecetaMedicaResponce>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaMedicaDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<RecetaMedicaResponce>builder()
                        .success(true)
                        .message("Receta médica actualizada")
                        .data(recetamedicaService.actualizar(id, dto, token))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        recetamedicaService.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Receta médica eliminada")
                        .build()
        );
    }
}
