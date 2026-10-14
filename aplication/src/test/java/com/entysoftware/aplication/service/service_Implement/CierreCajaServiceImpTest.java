package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperCierreCajaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.CierreCajaRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.services.CierreCajaServiceImp;

@ExtendWith(MockitoExtension.class)
class CierreCajaServiceImpTest {

    @Mock
    private CierreCajaRepository cierreCajaRepository;

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @Spy
    private MapperCierreCajaDto mapperCierreCajaDto = new MapperCierreCajaDto();

    @InjectMocks
    private CierreCajaServiceImp cierreCajaServiceImp;

    @Nested
    class CuadreCajaTests {

        @Test
        void givenCuadreValido_whenCuadreCaja_thenGuardaYCierraElEstablecimiento() {
            // Arrange
            CuadreCajaDto dto = new CuadreCajaDto(null, 1, 450000, 320000, 150000, null);
            Establecimiento establecimiento = new Establecimiento(1, "Local", "123", LocalDate.now(), "ABIERTO");
            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(cierreCajaRepository.existsPorEstablecimientoYFecha(eq(1), any(LocalDate.class))).thenReturn(false);
            when(cierreCajaRepository.save(any(CierreCaja.class)))
                .thenReturn(new CierreCaja(5, 1, 450000, 320000, 150000, LocalDateTime.now()));

            // Act
            ResponseEntity<CuadreCajaDto> response = cierreCajaServiceImp.cuadreCaja(dto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(5, response.getBody().getIdCierreCaja());
            verify(cierreCajaRepository, times(1)).save(any(CierreCaja.class));
            verify(establecimientoRepository, times(1)).actualizarEstadoEstablecimiento("CERRADO", 1);
        }

        @Test
        void givenCuadreYaRegistradoHoy_whenCuadreCaja_thenLanzaDatosDuplicadosYNoCierra() {
            // Arrange
            CuadreCajaDto dto = new CuadreCajaDto(null, 1, 0, 0, 0, null);
            Establecimiento establecimiento = new Establecimiento(1, "Local", "123", LocalDate.now(), "ABIERTO");
            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(cierreCajaRepository.existsPorEstablecimientoYFecha(eq(1), any(LocalDate.class))).thenReturn(true);

            // Act & Assert
            assertThrows(DatosDuplicados.class, () -> cierreCajaServiceImp.cuadreCaja(dto));
            verify(cierreCajaRepository, never()).save(any());
            verify(establecimientoRepository, never()).actualizarEstadoEstablecimiento(any(), anyInt());
        }

        @Test
        void givenEstablecimientoInexistente_whenCuadreCaja_thenLanzaNoEncontradoYNoCierra() {
            // Arrange
            CuadreCajaDto dto = new CuadreCajaDto(null, 99, 0, 0, 0, null);
            when(establecimientoRepository.findById(99)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class, () -> cierreCajaServiceImp.cuadreCaja(dto));
            verify(establecimientoRepository, never()).actualizarEstadoEstablecimiento(any(), anyInt());
        }
    }
}
