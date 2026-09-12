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
import com.entysoftware.aplication.mapper.MapperPagosVoucherDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosVoucherDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoVoucher;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.PagosVoucherRepository;
import com.entysoftware.aplication.service.services.PagosVoucherServiceImp;

@ExtendWith(MockitoExtension.class)
class PagosVoucherServiceImpTest {

    private static final LocalDateTime FECHA = LocalDateTime.of(2026, 9, 12, 14, 30);

    @Mock
    private PagosVoucherRepository pagosVoucherRepository;

    @Mock
    private MapperPagosVoucherDto mapperPagosVoucherDto;

    @Mock
    private EstablecimientoRepository establecimientoRepository;

    @InjectMocks
    private PagosVoucherServiceImp pagosVoucherServiceImp;

    private PagoVoucher crearPago(Integer id, Integer idEstablecimiento,Integer valor ,String codigo) {
        return new PagoVoucher(id, idEstablecimiento, codigo,valor ,FECHA);
    }

    @Nested
    class ListarPagosVoucherTests {

        @Test
        void givenSinFechas_whenListarPagosVoucher_thenRetornaPaginaSinFiltroOrdenadaPorFechaDesc() {
            // Arrange
            PagoVoucher pago = crearPago(1, 1, 45000, "VCH-1");
            PagosVoucherDto pagoDto = new PagosVoucherDto(1, 1, "VCH-1", 45000, FECHA);
            Pageable pageableEsperado = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "fecha"));
            Page<PagoVoucher> paginaPagos = new PageImpl<>(List.of(pago), pageableEsperado, 11);
            when(pagosVoucherRepository.findByIdEstablecimiento(1, pageableEsperado)).thenReturn(paginaPagos);
            when(mapperPagosVoucherDto.pagoVoucherToDto(pago)).thenReturn(pagoDto);

            // Act
            ResponseEntity<PaginaDto<PagosVoucherDto>> response = pagosVoucherServiceImp.listarPagosVoucher(1, null, null, 0, 10);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            PaginaDto<PagosVoucherDto> body = response.getBody();
            assertNotNull(body);
            assertEquals(List.of(pagoDto), body.getContenido());
            assertEquals(0, body.getPaginaActual());
            assertEquals(10, body.getTamanoPagina());
            assertEquals(11, body.getTotalElementos());
            assertEquals(2, body.getTotalPaginas());
            assertTrue(body.isPrimera());
            verify(pagosVoucherRepository, never()).buscarPorEstablecimientoYRangoFechas(any(), any(), any(), any());
        }

        @Test
        void givenRangoDeFechas_whenListarPagosVoucher_thenFiltraDesdeInicioDelDiaHastaFinDelDiaFinal() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2026, 9, 1);
            LocalDate fechaFin = LocalDate.of(2026, 9, 12);
            when(pagosVoucherRepository.buscarPorEstablecimientoYRangoFechas(eq(1), any(), any(), any())).thenReturn(Page.empty());

            // Act
            ResponseEntity<PaginaDto<PagosVoucherDto>> response = pagosVoucherServiceImp.listarPagosVoucher(1, fechaInicio, fechaFin, 0, 10);

            // Assert
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getContenido().isEmpty());
            verify(pagosVoucherRepository, times(1)).buscarPorEstablecimientoYRangoFechas(
                eq(1), eq(LocalDateTime.of(2026, 9, 1, 0, 0)), eq(LocalDateTime.of(2026, 9, 13, 0, 0)), any());
        }

        @Test
        void givenSoloFechaInicio_whenListarPagosVoucher_thenFiltraUnicamenteEseDia() {
            // Arrange
            LocalDate fecha = LocalDate.of(2026, 9, 12);
            when(pagosVoucherRepository.buscarPorEstablecimientoYRangoFechas(eq(1), any(), any(), any())).thenReturn(Page.empty());

            // Act
            pagosVoucherServiceImp.listarPagosVoucher(1, fecha, null, 0, 10);

            // Assert
            verify(pagosVoucherRepository, times(1)).buscarPorEstablecimientoYRangoFechas(
                eq(1), eq(LocalDateTime.of(2026, 9, 12, 0, 0)), eq(LocalDateTime.of(2026, 9, 13, 0, 0)), any());
        }

        @Test
        void givenFechaInicioPosteriorAFechaFin_whenListarPagosVoucher_thenLanzaRangoFechasInvalidoException() {
            // Arrange
            LocalDate fechaInicio = LocalDate.of(2026, 9, 12);
            LocalDate fechaFin = LocalDate.of(2026, 9, 1);

            // Act & Assert
            RangoFechasInvalidoException exception = assertThrows(RangoFechasInvalidoException.class,
                () -> pagosVoucherServiceImp.listarPagosVoucher(1, fechaInicio, fechaFin, 0, 10));
            assertEquals("La fecha de inicio 2026-09-12 no puede ser posterior a la fecha fin 2026-09-01", exception.getMessage());
            verify(pagosVoucherRepository, never()).buscarPorEstablecimientoYRangoFechas(any(), any(), any(), any());
        }

        @Test
        void givenPaginacionFueraDeRango_whenListarPagosVoucher_thenAjustaPaginaYTamanoALimitesValidos() {
            // Arrange
            when(pagosVoucherRepository.findByIdEstablecimiento(eq(1), any())).thenReturn(Page.empty());
            ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);

            // Act
            pagosVoucherServiceImp.listarPagosVoucher(1, null, null, -3, 500);

            // Assert
            verify(pagosVoucherRepository).findByIdEstablecimiento(eq(1), captor.capture());
            assertEquals(0, captor.getValue().getPageNumber());
            assertEquals(100, captor.getValue().getPageSize());
        }
    }

    @Nested
    class CrearPagoVoucherTests {

        @Test
        void givenEstablecimientoExistente_whenCrearPagoVoucher_thenRetornaPagoCreado() {
            // Arrange
            PagosVoucherDto pagoDto = new PagosVoucherDto(null, 1, "VCH-9", 45000, null);
            Establecimiento establecimiento = new Establecimiento(1, "Restaurante", "prop-1", null, "ACTIVO");
            PagoVoucher pagoSinGuardar = crearPago(null, 1, 45000, "VCH-9");
            PagoVoucher pagoGuardado = crearPago(9, 1, 45000, "VCH-9");
            PagosVoucherDto pagoDtoGuardado = new PagosVoucherDto(9, 1, "VCH-9", 45000, FECHA);

            when(establecimientoRepository.findById(1)).thenReturn(Optional.of(establecimiento));
            when(mapperPagosVoucherDto.dtoToPagoVoucher(pagoDto, establecimiento)).thenReturn(pagoSinGuardar);
            when(pagosVoucherRepository.save(pagoSinGuardar)).thenReturn(pagoGuardado);
            when(mapperPagosVoucherDto.pagoVoucherToDto(pagoGuardado)).thenReturn(pagoDtoGuardado);

            // Act
            ResponseEntity<PagosVoucherDto> response = pagosVoucherServiceImp.crearPagoVoucher(pagoDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(9, response.getBody().getIdVoucher());
            assertEquals(45000, response.getBody().getValor());
            verify(pagosVoucherRepository, times(1)).save(pagoSinGuardar);
        }

        @Test
        void givenEstablecimientoInexistente_whenCrearPagoVoucher_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            PagosVoucherDto pagoDto = new PagosVoucherDto(null, 50, "VCH-9", 45000, null);
            when(establecimientoRepository.findById(50)).thenReturn(Optional.empty());

            // Act & Assert
            ObjetosNoEncontradosExepcion exception = assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosVoucherServiceImp.crearPagoVoucher(pagoDto));
            assertEquals("El establecimiento con ID 50 no existe", exception.getMessage());
            verify(pagosVoucherRepository, never()).save(any());
        }
    }

    @Nested
    class EditarPagoVoucherTests {

        @Test
        void givenPagoExistente_whenEditarPagoVoucher_thenActualizaCodigoYValorSinModificarFechaNiEstablecimiento() {
            // Arrange
            PagoVoucher pagoExistente = crearPago(3, 1, 45000, "VCH-3");
            PagosVoucherDto pagoDto = new PagosVoucherDto(3, 99, "VCH-3-CORREGIDO", 50000, LocalDateTime.of(2020, 1, 1, 0, 0));
            when(pagosVoucherRepository.findById(3)).thenReturn(Optional.of(pagoExistente));

            // Act
            ResponseEntity<String> response = pagosVoucherServiceImp.editarPagoVoucher(pagoDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Pago con voucher actualizado", response.getBody());
            assertEquals("VCH-3-CORREGIDO", pagoExistente.getCodigo());
            assertEquals(50000, pagoExistente.getValor());
            assertEquals(1, pagoExistente.getIdEstablecimiento());
            assertEquals(FECHA, pagoExistente.getFecha());
            verify(pagosVoucherRepository, times(1)).save(pagoExistente);
        }

        @Test
        void givenValorNulo_whenEditarPagoVoucher_thenConservaValorExistente() {
            // Arrange
            PagoVoucher pagoExistente = crearPago(5, 1, 45000, "VCH-5");
            PagosVoucherDto pagoDto = new PagosVoucherDto(5, null, "VCH-5-CORREGIDO", null, null);
            when(pagosVoucherRepository.findById(5)).thenReturn(Optional.of(pagoExistente));

            // Act
            pagosVoucherServiceImp.editarPagoVoucher(pagoDto);

            // Assert
            assertEquals("VCH-5-CORREGIDO", pagoExistente.getCodigo());
            assertEquals(45000, pagoExistente.getValor());
            verify(pagosVoucherRepository, times(1)).save(pagoExistente);
        }

        @Test
        void givenPagoInexistente_whenEditarPagoVoucher_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            PagosVoucherDto pagoDto = new PagosVoucherDto(99, null, "VCH-X", 45000, null);
            when(pagosVoucherRepository.findById(99)).thenReturn(Optional.empty());

            // Act & Assert
            ObjetosNoEncontradosExepcion exception = assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosVoucherServiceImp.editarPagoVoucher(pagoDto));
            assertEquals("El pago con voucher con ID 99 no existe", exception.getMessage());
            verify(pagosVoucherRepository, never()).save(any());
        }
    }

    @Nested
    class EliminarPagoVoucherTests {

        @Test
        void givenPagoExistente_whenEliminarPagoVoucher_thenEliminaYRetornaMensaje() {
            // Arrange
            when(pagosVoucherRepository.existsById(4)).thenReturn(true);

            // Act
            ResponseEntity<String> response = pagosVoucherServiceImp.eliminarPagoVoucher(4);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Pago con voucher eliminado", response.getBody());
            verify(pagosVoucherRepository, times(1)).deleteById(4);
        }

        @Test
        void givenPagoInexistente_whenEliminarPagoVoucher_thenLanzaObjetosNoEncontradosExepcion() {
            // Arrange
            when(pagosVoucherRepository.existsById(99)).thenReturn(false);

            // Act & Assert
            assertThrows(ObjetosNoEncontradosExepcion.class,
                () -> pagosVoucherServiceImp.eliminarPagoVoucher(99));
            verify(pagosVoucherRepository, never()).deleteById(any());
        }
    }
}
