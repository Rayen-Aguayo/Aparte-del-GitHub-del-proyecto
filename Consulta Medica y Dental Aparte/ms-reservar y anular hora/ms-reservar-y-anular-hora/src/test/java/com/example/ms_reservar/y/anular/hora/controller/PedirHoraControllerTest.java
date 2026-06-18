package com.example.ms_reservar.y.anular.hora.controller;

import com.example.ms_reservar.y.anular.hora.dto.MedicoResponse;
import com.example.ms_reservar.y.anular.hora.dto.PacienteResponse;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraDTO;
import com.example.ms_reservar.y.anular.hora.dto.PedirHoraResponse;
import com.example.ms_reservar.y.anular.hora.service.PedirHoraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
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

    private PedirHoraDTO crearDtoDePrueba() {
        PedirHoraDTO dto = new PedirHoraDTO();
        dto.setRunPaciente("11111111-1");
        dto.setNombrePaciente("Juan Pérez");
        dto.setRunMedico("22222222-2");
        dto.setNombreMedico("Dra. Soto");
        dto.setFecha(LocalDate.of(2026, 6, 20));
        dto.setHoraDeAtencion(LocalTime.of(10, 30));
        dto.setAtencion("Consulta dental");
        return dto;
    }

    private PedirHoraResponse respuestaDePrueba(Long id) {
        PacienteResponse paciente = new PacienteResponse();
        paciente.setRunPaciente("11111111-1");
        paciente.setNombrePaciente("Juan Pérez");

        MedicoResponse medico = new MedicoResponse();
        medico.setRunMedico("22222222-2");
        medico.setNombreMedico("Dra. Soto");
        medico.setEspecialidad("Odontología");

        return PedirHoraResponse.builder()
                .id(id)
                .paciente(paciente)
                .medico(medico)
                .fecha(LocalDate.of(2026, 6, 20))
                .horaDeAtencion(LocalTime.of(10, 30))
                .atencion("Consulta dental")
                .build();
    }

    @Test
    void debeCrearReserva() throws Exception {
        PedirHoraDTO dto = crearDtoDePrueba();
        PedirHoraResponse creado = respuestaDePrueba(1L);

        when(service.crear(any(PedirHoraDTO.class), anyString())).thenReturn(creado);

        mockMvc.perform(post("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se reservo la hora"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paciente.nombrePaciente").value("Juan Pérez"))
                .andExpect(jsonPath("$.data.medico.nombreMedico").value("Dra. Soto"))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"));
    }

    @Test
    void debeRechazarCreacionConDatosInvalidos() throws Exception {
        PedirHoraDTO dto = new PedirHoraDTO(); // todos los campos vacíos/nulos

        mockMvc.perform(post("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void debeListarReservas() throws Exception {
        when(service.listar(anyString())).thenReturn(List.of(respuestaDePrueba(1L)));

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Listado obtenido"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].atencion").value("Consulta dental"));
    }

    @Test
    void debeObtenerReservaPorId() throws Exception {
        when(service.obtener(1L, token)).thenReturn(respuestaDePrueba(1L));

        mockMvc.perform(get("/api/v1/reservar-y-anular-hora/1")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.atencion").value("Consulta dental"));
    }

    @Test
    void debeActualizarReserva() throws Exception {
        PedirHoraDTO dto = crearDtoDePrueba();
        dto.setAtencion("Control");

        PedirHoraResponse actualizado = respuestaDePrueba(1L);
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
    void debeEliminarReserva() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/reservar-y-anular-hora/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Se anulo la hora"));
    }
}