package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperCierreCajaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.models.BaseInicial;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CierreCajaRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CierreDiaInterface;
import com.entysoftware.aplication.service.services.CierreCajaServiceImp;
import com.entysoftware.aplication.utils.GeneradorPdfCierreCaja;

@ExtendWith(MockitoExtension.class)
class CierreCajaServiceImpTest {

    @Mock
    private CierreCajaRepository cierreCajaRepository;

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @Mock
    private BaseInicialRepository baseInicialRepository;

    @Mock
    private CierreDiaInterface cierreDiaInterface;

    @Spy
    private MapperCierreCajaDto mapperCierreCajaDto = new MapperCierreCajaDto();

    @Spy
    private GeneradorPdfCierreCaja generadorPdfCierreCaja = new GeneradorPdfCierreCaja();

    @InjectMocks
    private CierreCajaServiceImp cierreCajaServiceImp;

    @Nested
    class CuadreCajaTests {

        @Test
        void givenCuadreValido_whenCuadreCaja_thenGuardaCierraYDevuelveElPdf() throws IOException {
            // Arrange
            CuadreCajaDto dto = new CuadreCajaDto(null, 1, 430000, 300000, 150000, null);
            Establecimiento establecimiento = new Establecimiento(1, "Local Centro", "123", LocalDate.now(), "ABIERTO");
            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(cierreCajaRepository.existsPorEstablecimientoYFecha(eq(1), any(LocalDate.class))).thenReturn(false);
            when(cierreCajaRepository.save(any(CierreCaja.class)))
                .thenReturn(new CierreCaja(5, 1, 430000, 300000, 150000, LocalDateTime.now()));
            // Esperado: efectivo 310000 (50000 + 320000 - 60000), transferencia 430000, tarjeta 150000
            CierreDiaDto cierreDia = new CierreDiaDto(34, 450000, 320000, 50000, 60000, 20000, 310000, 430000, 150000, 25000);
            when(cierreDiaInterface.cierreDia(1)).thenReturn(ResponseEntity.ok(cierreDia));
            when(baseInicialRepository.validarExistenciaBaseInicial(eq(1), any(LocalDate.class)))
                .thenReturn(new BaseInicial(1, 1, 50000, LocalDate.now(), LocalTime.of(8, 0)));

            // Act
            ResponseEntity<byte[]> response = cierreCajaServiceImp.cuadreCaja(dto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
            assertTrue(response.getHeaders().getContentDisposition().getFilename().startsWith("cierre-caja-1-"));
            verify(cierreCajaRepository, times(1)).save(any(CierreCaja.class));
            verify(establecimientoRepository, times(1)).actualizarEstadoEstablecimiento("CERRADO", 1);

            try (PDDocument pdf = Loader.loadPDF(response.getBody())) {
                String texto = new PDFTextStripper().getText(pdf);
                assertTrue(texto.contains("Local Centro"));
                assertTrue(texto.contains("$ 310.000"));
                assertTrue(texto.contains("Faltante en caja: $ 10.000"));
            }
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
