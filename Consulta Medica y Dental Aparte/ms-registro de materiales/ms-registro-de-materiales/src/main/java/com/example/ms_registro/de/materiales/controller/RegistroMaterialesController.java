package com.example.ms_registro.de.materiales.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_registro.de.materiales.dto.ApiResponse;
import com.example.ms_registro.de.materiales.model.RegistroMateriales;
import com.example.ms_registro.de.materiales.service.RegistroMaterialesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/registrosMateriales")
@RequiredArgsConstructor
public class RegistroMaterialesController {
    private final RegistroMaterialesService pacienteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> crear(@Valid @RequestBody RegistroMaterialesDTO dto) {

        RegistroMateriales registro = pacienteService.crear(dto);

        return ResponseEntity.status(201).body(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro creado")
                        .data(registro)
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RegistroMateriales>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.<List<RegistroMateriales>>builder()
                        .success(true)
                        .message("Listado obtenido")
                        .data(pacienteService.listar())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro obtenido")
                        .data(pacienteService.obtener(id))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistroMateriales>> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody RegistroMaterialesDTO dto) {

        RegistroMateriales registro = pacienteService.actualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<RegistroMateriales>builder()
                        .success(true)
                        .message("registro actualizado")
                        .data(registro)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        pacienteService.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("registro eliminado")
                        .build()
        );
    }
}
