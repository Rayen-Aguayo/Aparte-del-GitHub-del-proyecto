package com.example.ms_reservar.y.anular.hora.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_reservar.y.anular.hora.model.PedirHora;
import com.example.ms_reservar.y.anular.hora.repository.PedirHoraRepository;
import com.example.ms_reservar.y.anular.hora.service.PedirHoraService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private PedirHoraRepository repo;

    @InjectMocks
    private PedirHoraService service;
}
