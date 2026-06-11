package com.example.ms_medico.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_medico.dto.ApiResponse;
import com.example.ms_medico.dto.MedicoDTO;
import com.example.ms_medico.model.Medico;
import com.example.ms_medico.service.MedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/v1/medicos")
@RequiredArgsConstructor
public class MedicoController {
    private final MedicoService medicoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medico>> crear(@Valid @RequestBody MedicoDTO dto) {

        Medico medico = medicoService.crear(dto);
         return ResponseEntity.status(201).body(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico creado")
                        .data(medico)
                        .build()
        );
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<List<Medico>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<Medico>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(medicoService.listar())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medico>> obtener(@PathVariable String id) {

        return ResponseEntity.ok(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico obtenido")
                        .data(medicoService.obtener(id))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Medico>> actualizar(@PathVariable String id, @Valid @RequestBody MedicoDTO dto) {

        Medico medico = medicoService.actualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<Medico>builder()
                        .success(true)
                        .message("Medico actualizado")
                        .data(medico)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable String id) {

        medicoService.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Medico eliminado")
                        .build()
        );
    }
}

