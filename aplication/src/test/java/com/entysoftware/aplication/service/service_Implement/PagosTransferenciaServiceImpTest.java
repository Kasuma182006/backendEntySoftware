package com.entysoftware.aplication.service.service_Implement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.error.RangoFechasInvalidoException;
import com.entysoftware.aplication.mapper.MapperPagosTransferenciaDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosTransferenciaDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoTransferencia;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.PagosTransferenciaRepository;
import com.entysoftware.aplication.service.services.PagosTransferenciaServiceImp;

@ExtendWith(MockitoExtension.class)
class PagosTransferenciaServiceImpTest {

    private static final LocalDateTime FECHA = LocalDateTime.of(2026, 9, 12, 14, 30);

    @Mock
    private PagosTransferenciaRepository pagosTransferenciaRepository;

    @Mock
    private MapperPagosTransferenciaDto mapperPagosTransferenciaDto;

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @InjectMocks
    private PagosTransferenciaServiceImp pagosTransferenciaServiceImp;

    private PagoTransferencia crearPago(Integer id, Integer idEstablecimiento, String codigo, Integer valor, String entidad) {
        return new PagoTransferencia(id, idEstablecimiento, codigo, valor, entidad, FECHA);
    }

    @Nested
    class ListarPagosTransferenciaTests {

        @Test
        void givenSinFechas_whenListarPagosTransferencia_thenRetornaPaginaSinFiltroOrdenadaPorFechaDesc() {
            // Arrange
            PagoTransferencia pago = crearPago(1, 1, "TRX-1", 45000, "NEQUI");
            PagosTransferenciaDto pagoDto = new PagosTransferenciaDto(1, 1, "TRX-1", 45000, "NEQUI", FECHA);
            Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "fecha"));
            Page<PagoTransferencia> paginaPagos = new PageImpl<>(List.of(pago), pageableEsperado, 11);
            when(pagosTransferenciaRepository.findByIdEstablecimiento(1, pageableEsperado)).thenReturn(paginaPagos);
            when(mapperPagosTransferenciaDto.pagoTransferenciaToDto(pago)).thenReturn(pagoDto);

            // Act
            ResponseEntity<PaginaDto<PagosTransferenciaDto>> response = pagosTransferenciaServiceImp.listarPagosTransferencia(1, null, null, 0, 10);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            PaginaDto<PagosTransferenciaDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(List.of(pagoDto), body.getContenido());
            assertEquals(0, body.getPaginaActual());
            assertEquals(10, body.getTamanoPagina());
            assertEquals(11, body.getTotalElementos());
            assertEquals(2, body.getTotalPaginas());
            assertTrue(body.isPrimera());
            verify(pagosTransferenciaRepository, never()).buscarPorEstablecimientoYRangoFechas(any(), any(), any(), any());
        }

        @Test
        void givenRangoDeFechas_whenListarPagosTransferencia_thenFiltraDesdeInicioDelDiaHastaFinDelDiaFinal() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2026, 9, 1);
            LocalDate fechaFin = LocalDate.of(2026, 9, 12);
            when(pagosTransferenciaRepository.buscarPorEstablecimientoYRangoFechas(eq(1), any(), any(), any())).thenReturn(Page.empty());

            // Act
            ResponseEntity<PaginaDto<PagosTransferenciaDto>> response = pagosTransferenciaServiceImp.listarPagosTransferencia(1, fechaInicio, fechaFin, 0, 10);

            // Assert
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getContenido().isEmpty());
            verify(pagosTransferenciaRepository, times(1)).buscarPorEstablecimientoYRangoFechas(
                eq(1), eq(LocalDateTime.of(2026, 9, 1, 0, 0)), eq(LocalDateTime.of(2026, 9, 13, 0, 0)), any());
            verify(pagosTransferenciaRepository, never()).findByIdEstablecimiento(any(), any());
        }

        @Test
        void givenSoloFechaFin_whenListarPagosTransferencia_thenFiltraUnicamenteEseDia() {
            // Arrange
            LocalDate fecha = LocalDate.of(2026, 9, 12);
            when(pagosTransferenciaRepository.buscarPorEstablecimientoYRangoFechas(eq(1), any(), any(), any())).thenReturn(Page.empty());

            // Act
            pagosTransferenciaServiceImp.listarPagosTransferencia(1, null, fecha, 0, 10);

            // Assert
            verify(pagosTransferenciaRepository, times(1)).buscarPorEstablecimientoYRangoFechas(
                eq(1), eq(LocalDateTime.of(2026, 9, 12, 0, 0)), eq(LocalDateTime.of(2026, 9, 13, 0, 0)), any());
        }

        @Test
        void givenFechaInicioPosteriorAFechaFin_whenListarPagosTransferencia_thenLanzaRangoFechasInvalidoException() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2026, 9, 12);
            LocalDate fechaFin = LocalDate.of(2026, 9, 1);

            // Act & Assert
            RangoFechasInvalidoException exception = assertThrows(RangoFechasInvalidoException.class,
                () -> pagosTransferenciaServiceImp.listarPagosTransferencia(1, fechaInicio, fechaFin, 0, 10));
            assertEquals("La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01", exception.getMessage());
            verify(pagosTransferenciaRepository, never()).buscarPorEstablecimientoYRangoFechas(any(), any(), any(), any());
        }

        @Test
        void givenPaginacionFueraDeRango_whenListarPagosTransferencia_thenAjustaPaginaYTamanoALimitesValidos() {
            // Arrange
            when(pagosTransferenciaRepository.findByIdEstablecimiento(eq(1), any())).thenReturn(Page.empty());
            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

            // Act
            pagosTransferenciaServiceImp.listarPagosTransferencia(1, null, null, -3, 500);

            // Assert
            verify(pagosTransferenciaRepository).findByIdEstablecimiento(eq(1), captor.capture());
            assertEquals(0, captor.getValue().getPageNumber());
            assertEquals(100, captor.getValue().getPageSize());
        }
    }

    @Nested
    class CrearPagoTransferenciaTests {

        @Test
        void givenEstablecimientoExistente_whenCrearPagoTransferencia_thenRetornaPagoCreado() {
            // Arrange
            PagosTransferenciaDto pagoDto = new PagosTransferenciaDto(null, 1, "TRX-9", 45000, "NEQUI", null);
            Establecimiento establecimiento = new Establecimiento(1, "Restaurante", "prop-1", null, "ACTIVO");
            PagoTransferencia pagoSinGuardar = crearPago(null, 1, "TRX-9", 45000, "NEQUI");
            PagoTransferencia pagoGuardado = crearPago(9, 1, "TRX-9", 45000, "NEQUI");
            PagosTransferenciaDto pagoDtoGuardado = new PagosTransferenciaDto(9, 1, "TRX-9", 45000, "NEQUI", FECHA);

            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(mapperPagosTransferenciaDto.dtoToPagoTransferencia(pagoDto, establecimiento)).thenReturn(pagoSinGuardar);
            when(pagosTransferenciaRepository.save(pagoSinGuardar)).thenReturn(pagoGuardado);
            when(mapperPagosTransferenciaDto.pagoTransferenciaToDto(pagoGuardado)).thenReturn(pagoDtoGuardado);

            // Act
            ResponseEntity<PagosTransferenciaDto> response = pagosTransferenciaServiceImp.crearPagoTransferencia(pagoDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(9, response.getBody().getIdTransferencia());
            verify(pagosTransferenciaRepository, times(1)).save(pagoSinGuardar);
        }

        @Test
        void givenEstablecimientoInexistente_whenCrearPagoTransferencia_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            PagosTransferenciaDto pagoDto = new PagosTransferenciaDto(null, 50, "TRX-9", 45000, "NEQUI", null);
            when(establecimientoRepository.findById(50)).thenReturn(Optional.empty());

            // Act & Assert
            ObjetosNoEncontradosExepcion exception = assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosTransferenciaServiceImp.crearPagoTransferencia(pagoDto));
            assertEquals("El establecimiento con ID 50 no existe", exception.getMessage());
            verify(pagosTransferenciaRepository, never()).save(any());
        }
    }

    @Nested
    class EditarPagoTransferenciaTests {

        @Test
        void givenPagoExistenteYCamposParciales_whenEditarPagoTransferencia_thenActualizaSoloCamposNoNulos() {
            // Arrange
            PagoTransferencia pagoExistente = crearPago(3, 1, "TRX-3", 20000, "NEQUI");
            PagosTransferenciaDto pagoDto = new PagosTransferenciaDto(3, null, null, 25000, null, null);
            when(pagosTransferenciaRepository.findById(3)).thenReturn(Optional.of(pagoExistente));

            // Act
            ResponseEntity<String> response = pagosTransferenciaServiceImp.editarPagoTransferencia(pagoDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Pago por transferencia actualizado", response.getBody());
            assertEquals(25000, pagoExistente.getValor());
            assertEquals("TRX-3", pagoExistente.getCodigo());
            assertEquals("NEQUI", pagoExistente.getEntidadFinanciera());
            assertEquals(1, pagoExistente.getIdEstablecimiento());
            verify(pagosTransferenciaRepository, times(1)).save(pagoExistente);
        }

        @Test
        void givenPagoInexistente_whenEditarPagoTransferencia_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            PagosTransferenciaDto pagoDto = new PagosTransferenciaDto(99, null, "TRX-X", null, null, null);
            when(pagosTransferenciaRepository.findById(99)).thenReturn(Optional.empty());

            // Act & Assert
            ObjetosNoEncontradosExepcion exception = assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosTransferenciaServiceImp.editarPagoTransferencia(pagoDto));
            assertEquals("El pago por transferencia con ID 99 no existe", exception.getMessage());
            verify(pagosTransferenciaRepository, never()).save(any());
        }
    }

    @Nested
    class EliminarPagoTransferenciaTests {

        @Test
        void givenPagoExistente_whenEliminarPagoTransferencia_thenEliminaYRetornaMensaje() {
            // Arrange
            when(pagosTransferenciaRepository.existsById(4)).thenReturn(true);

            // Act
            ResponseEntity<String> response = pagosTransferenciaServiceImp.eliminarPagoTransferencia(4);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Pago por transferencia eliminado", response.getBody());
            verify(pagosTransferenciaRepository, times(1)).deleteById(4);
        }

        @Test
        void givenPagoInexistente_whenEliminarPagoTransferencia_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            when(pagosTransferenciaRepository.existsById(99)).thenReturn(false);

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosTransferenciaServiceImp.eliminarPagoTransferencia(99));
            verify(pagosTransferenciaRepository, never()).deleteById(any());
        }
    }
}
