package com.example.ms_reservar.y.anular.hora.controller;

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

import com.example.ms_reservar.y.anular.hora.dto.ApiResponse;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reservar.y.anular.hora.service.PedirHoraService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservar-y-anular-hora")
@RequiredArgsConstructor

public class PedirHoraController { 
    private final PedirHoraService pedirHoraService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> crear(
            @Valid @RequestBody PedirHoraDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .message("Se reservo la hora")
                        .data(pedirHoraService.crear(dto, token))
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PedirHoraResponse>>> listar(
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<List<PedirHoraResponse>>builder()
                        .success(true)
                        .data(pedirHoraService.listar(token))
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .data(pedirHoraService.obtener(id, token))
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<PedirHoraResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedirHoraDTO dto,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(
                ApiResponse.<PedirHoraResponse>builder()
                        .success(true)
                        .message("Se cambio la hora")
                        .data(pedirHoraService.actualizar(id, dto, token))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        pedirHoraService.eliminar(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Se anulo la hora")
                        .build()
        );
    }
}