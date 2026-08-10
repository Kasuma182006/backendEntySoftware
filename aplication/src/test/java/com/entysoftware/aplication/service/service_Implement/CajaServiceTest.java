package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.BaseInicial;
import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;

@ExtendWith(MockitoExtension.class)
class CajaServiceTest {

    @Mock
    private BaseInicialRepository baseInicialRepository;

    @InjectMocks
    private CajaService cajaService;

    @Nested
    class AperturaDiaTests {

        @Test
        void givenBaseValida_whenAperturaDia_thenRetornaRespuestaConDatosGuardados() {
            // Arrange
            BaseInicialDto baseDto = new BaseInicialDto(1, 50000);
            LocalDate fechaGuardada = LocalDate.of(2026, 8, 10);
            LocalTime horaGuardada = LocalTime.of(10, 30);
            BaseInicial baseGuardada = new BaseInicial(1, 1, 50000, fechaGuardada, horaGuardada);
            when(baseInicialRepository.save(any(BaseInicial.class))).thenReturn(baseGuardada);

            // Act
            ResponseEntity<RespuestaBaseInicialDto> response = cajaService.aperturaDia(baseDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            RespuestaBaseInicialDto body = response.getBody();
            assertNotNull(body);
            assertTrue(body.isAbierto());
            assertEquals(50000, body.getBaseInicial());
            assertEquals(horaGuardada, body.getHoraApertura());
            assertEquals(fechaGuardada, body.getFecha());
            verify(baseInicialRepository, times(1)).save(any(BaseInicial.class));
        }

        @Test
        void givenBaseDto_whenAperturaDia_thenConstruyeBaseInicialConDatosDelDtoYFechaActual() {
            // Arrange
            BaseInicialDto baseDto = new BaseInicialDto(5, 20000);
            ArgumentCaptor<BaseInicial> captor = ArgumentCaptor.forClass(BaseInicial.class);
            when(baseInicialRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            cajaService.aperturaDia(baseDto);

            // Assert
            BaseInicial capturado = captor.getValue();
            assertNull(capturado.getIdBase());
            assertEquals(5, capturado.getIdEstablecimiento());
            assertEquals(20000, capturado.getValorBaseInicial());
            assertEquals(LocalDate.now(), capturado.getFecha());
            verify(baseInicialRepository, times(1)).save(any(BaseInicial.class));
        }
    }
}
