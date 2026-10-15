package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.entysoftware.aplication.service.services.CierreDiaServiceImp;

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
        void givenMovimientosDelDia_whenCierreDia_thenCalculaNetosIncluyendoBaseYDomicilios() {
            // Arrange
            when(baseInicialRepository.buscarValorBaseInicialHoy(eq(1), any(LocalDate.class))).thenReturn(50000);
            when(encabezadoPedidosRepository.sumarIngresosEfectivoDelDia(eq(1), any(LocalDate.class))).thenReturn(320000);
            when(encabezadoPedidosRepository.sumarIngresosTransferenciaDelDia(eq(1), any(LocalDate.class))).thenReturn(450000);
            when(encabezadoPedidosRepository.sumarIngresosTarjetaDelDia(eq(1), any(LocalDate.class))).thenReturn(150000);
            when(encabezadoPedidosRepository.sumarIngresoDomicilio(eq(1), any(LocalDate.class))).thenReturn(25000);
            when(encabezadoPedidosRepository.contarPedidosDelDia(eq(1), any(LocalDate.class))).thenReturn(34);
            when(costosRepository.sumarGastosEfectivoDelDia(eq(1), any(LocalDate.class))).thenReturn(60000);
            when(costosRepository.sumarGastosTransferenciaDelDia(eq(1), any(LocalDate.class))).thenReturn(20000);

            // Act
            ResponseEntity<CierreDiaDto> response = cierreDiaServiceImp.cierreDia(1);

            // Assert
            CierreDiaDto cierre = response.getBody();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(310000, cierre.getCantidadIngresosTotalesEnEfectivoNeta()); // 50000 + 320000 - 60000
            assertEquals(430000, cierre.getCantidadIngresosEnTrasferenciaNeta());   // 450000 - 20000
            assertEquals(150000, cierre.getCantidadIngresosEnTarjeta());
            assertEquals(25000, cierre.getCantidadDomicilios());
            assertEquals(34, cierre.getCantidadPedidosRealizadosHoy());
        }
    }
}
