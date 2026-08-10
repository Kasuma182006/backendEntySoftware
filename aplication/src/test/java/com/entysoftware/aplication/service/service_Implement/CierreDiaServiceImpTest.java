package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CostosRepository;
import com.entysoftware.aplication.repository.EncabezadoPedidosRepository;

@ExtendWith(MockitoExtension.class)
class CierreDiaServiceImpTest {

    @Mock
    private EncabezadoPedidosRepository encabezadoPedidosRepository;

    @Mock
    private CostosRepository costosRepository;

    @Mock
    private BaseInicialRepository baseInicialRepository;

    @InjectMocks
    private CierreDiaServiceImp cierreDiaServiceImp;

    @Nested
    class CierreDiaTests {

        @Test
        void givenVentasYCostosDelDia_whenCierreDia_thenCalculaTotalesYNetosCorrectamente() {
            // Arrange
            Integer idEstablecimiento = 1;
            when(encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(100000);
            when(encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(50000);
            when(baseInicialRepository.buscarValorBaseInicialHoy(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(20000);
            when(costosRepository.sumarGastosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(10000);
            when(costosRepository.sumarGastosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(5000);
            when(encabezadoPedidosRepository.contarPedidosDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(7);

            // Act
            ResponseEntity<CierreDiaDto> response = cierreDiaServiceImp.cierreDia(idEstablecimiento);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            CierreDiaDto dto = response.getBody();
            assertNotNull(dto);
            assertEquals(7, dto.getCantidadPedidosRealizadosHoy());
            assertEquals(50000, dto.getCantidadIngresosEnTrasferenciaSinRestarCostos());
            assertEquals(100000, dto.getCantidadIngresosTotalesEnEfectivoSinRestarCostos());
            assertEquals(20000, dto.getBaseInicialDelDia());
            assertEquals(170000, dto.getCantidadGananciaTotalDelDiaSinRestarCostos());
            assertEquals(10000, dto.getCantidadCostosEnEfectivo());
            assertEquals(5000, dto.getCantidadCostosEnTrasferencia());
            assertEquals(15000, dto.getCantidadCostosTotalesDelDia());
            assertEquals(90000, dto.getCantidadIngresosTotalesEnEfectivoNeta());
            assertEquals(45000, dto.getCantidadIngresosEnTrasferenciaNeta());
            assertEquals(155000, dto.getCantidadGananciaNetaDelDia());

            verify(encabezadoPedidosRepository, times(1)).sumarIngresosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class));
            verify(encabezadoPedidosRepository, times(1)).sumarIngresosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class));
            verify(baseInicialRepository, times(1)).buscarValorBaseInicialHoy(eq(idEstablecimiento), any(LocalDate.class));
            verify(costosRepository, times(1)).sumarGastosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class));
            verify(costosRepository, times(1)).sumarGastosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class));
            verify(encabezadoPedidosRepository, times(1)).contarPedidosDelDia(eq(idEstablecimiento), any(LocalDate.class));
        }

        @Test
        void givenSinVentasNiCostosNiPedidos_whenCierreDia_thenRetornaTotalesEnCero() {
            // Arrange
            Integer idEstablecimiento = 2;
            when(encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);
            when(encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);
            when(baseInicialRepository.buscarValorBaseInicialHoy(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);
            when(costosRepository.sumarGastosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);
            when(costosRepository.sumarGastosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);
            when(encabezadoPedidosRepository.contarPedidosDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(0);

            // Act
            ResponseEntity<CierreDiaDto> response = cierreDiaServiceImp.cierreDia(idEstablecimiento);

            // Assert
            CierreDiaDto dto = response.getBody();
            assertNotNull(dto);
            assertEquals(0, dto.getCantidadPedidosRealizadosHoy());
            assertEquals(0, dto.getCantidadGananciaTotalDelDiaSinRestarCostos());
            assertEquals(0, dto.getCantidadGananciaNetaDelDia());
        }

        @Test
        void givenBaseInicialNoRegistradaParaHoy_whenCierreDia_thenLanzaNullPointerException() {
            // Arrange: sin base inicial registrada el repositorio retorna null para la suma
            Integer idEstablecimiento = 3;
            when(encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(1000);
            when(encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(500);
            when(baseInicialRepository.buscarValorBaseInicialHoy(eq(idEstablecimiento), any(LocalDate.class))).thenReturn(null);

            // Act & Assert
            assertThrows(NullPointerException.class, () -> cierreDiaServiceImp.cierreDia(idEstablecimiento));
            verify(costosRepository, never()).sumarGastosEfectivoDelDia(any(), any());
        }
    }
}
