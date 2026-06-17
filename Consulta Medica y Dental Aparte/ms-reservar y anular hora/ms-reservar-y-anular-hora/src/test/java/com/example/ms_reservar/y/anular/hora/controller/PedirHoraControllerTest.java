package com.example.ms_reservar.y.anular.hora.controller;

import com.example.ms_reservar.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reservar.y.anular.hora.service.PedirHoraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedirHoraController.class)
@AutoConfigureMockMvc(addFilters = false)
class PedirHoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PedirHoraService service;

    private final String token = "Bearer token-de-prueba";

    @Test
    void debeCrearReserva() throws Exception {
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHoraDeAtencion(LocalTime.of(10, 30));
        dto.setAtencion("Consulta dental");

        PedirHoraResponse creado = new PedirHoraResponse();
        creado.setId(1L);
        creado.setFecha(dto.getFecha());
        creado.setHoraDeAtencion(dto.getHoraDeAtencion());
        creado.setAtencion("Consulta dental");

        when(service.crear(any(PedirHoraDTO.class), anyString())).thenReturn(creado);

        mockMvc.perform(post("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se reservo la hora"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void debeListarReservas() throws Exception {
        PedirHoraResponse reserva = new PedirHoraResponse();
        reserva.setId(1L);
        reserva.setAtencion("Consulta dental");

        when(service.listar(anyString())).thenReturn(List.of(reserva));

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].atencion").value("Consulta dental"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void debeObtenerReservaPorId() throws Exception {
        PedirHoraResponse reserva = new PedirHoraResponse();
        reserva.setId(1L);
        reserva.setAtencion("Consulta dental");

        when(service.obtener(1L, token)).thenReturn(reserva);

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void debeActualizarReserva() throws Exception {
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 25));
        dto.setHoraDeAtencion(LocalTime.of(11, 0));
        dto.setAtencion("Control");

        PedirHoraResponse actualizado = new PedirHoraResponse();
        actualizado.setId(1L);
        actualizado.setFecha(dto.getFecha());
        actualizado.setHoraDeAtencion(dto.getHoraDeAtencion());
        actualizado.setAtencion("Control");

        when(service.actualizar(eq(1L), any(PedirHoraDTO.class), anyString())).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/reservar-y-anular-hora/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se cambio la hora"))
                .andExpect(jsonPath("$.data.atencion").value("Control"));
    }

    @Test
    @WithMockUser(roles = {"USER", "ADMIN"})
    void debeEliminarReserva() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/reservar-y-anular-hora/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se anulo la hora"));
    }
}