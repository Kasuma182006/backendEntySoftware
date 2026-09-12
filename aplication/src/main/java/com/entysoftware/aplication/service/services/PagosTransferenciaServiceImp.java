package com.entysoftware.aplication.service.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperPagosTransferenciaDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosTransferenciaDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoTransferencia;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.PagosTransferenciaRepository;
import com.entysoftware.aplication.service.interfaces.PagosTransferenciaInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;
import com.entysoftware.aplication.utils.RangoFechas;

import jakarta.transaction.Transactional;

@Service
public class PagosTransferenciaServiceImp implements PagosTransferenciaInterface {

    private static final String CAMPO_ORDEN = "fecha";

    private final PagosTransferenciaRepository pagosTransferenciaRepository;

    private final MapperPagosTransferenciaDto mapperPagosTransferenciaDto;

    private final EstablecimientoRepository establecimientoRepository;

    public PagosTransferenciaServiceImp(PagosTransferenciaRepository pagosTransferenciaRepository,
            MapperPagosTransferenciaDto mapperPagosTransferenciaDto, EstablecimientoRepository establecimientoRepository) {
        this.pagosTransferenciaRepository = pagosTransferenciaRepository;
        this.mapperPagosTransferenciaDto = mapperPagosTransferenciaDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    /**
     * Lista paginada de transferencias de un establecimiento, de la más reciente a la más antigua.
     * Sin fechas se listan todas; con una sola fecha se filtra ese día; con ambas se filtra el rango (inclusivo).
     */
    public ResponseEntity<PaginaDto<PagosTransferenciaDto>> listarPagosTransferencia(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoDesc(pagina, tamano, CAMPO_ORDEN);

        Page<PagoTransferencia> paginaPagos = RangoFechas.resolver(fechaInicio, fechaFin)
            .map(rango -> pagosTransferenciaRepository.buscarPorEstablecimientoYRangoFechas(idEstablecimiento, rango.desde(), rango.hasta(), pageable))
            .orElseGet(() -> pagosTransferenciaRepository.findByIdEstablecimiento(idEstablecimiento, pageable));

        return ResponseEntity.ok(PaginaDto.desdePage(paginaPagos.map(mapperPagosTransferenciaDto::pagoTransferenciaToDto)));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<PagosTransferenciaDto> crearPagoTransferencia(PagosTransferenciaDto pagoTransferenciaDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(pagoTransferenciaDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + pagoTransferenciaDto.getIdEstablecimiento() + " no existe"));

        PagoTransferencia pagoTransferencia = mapperPagosTransferenciaDto.dtoToPagoTransferencia(pagoTransferenciaDto, establecimiento);
        PagoTransferencia pagoGuardado = pagosTransferenciaRepository.save(pagoTransferencia);

        return ResponseEntity.ok(mapperPagosTransferenciaDto.pagoTransferenciaToDto(pagoGuardado));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarPagoTransferencia(PagosTransferenciaDto pagoTransferenciaDto) {
        PagoTransferencia pagoExistente = pagosTransferenciaRepository.findById(pagoTransferenciaDto.getIdTransferencia())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion(mensajePagoNoEncontrado(pagoTransferenciaDto.getIdTransferencia())));

        if (pagoTransferenciaDto.getCodigo() != null) {
            pagoExistente.setCodigo(pagoTransferenciaDto.getCodigo());
        }
        if (pagoTransferenciaDto.getValor() != null) {
            pagoExistente.setValor(pagoTransferenciaDto.getValor());
        }
        if (pagoTransferenciaDto.getEntidadFinanciera() != null) {
            pagoExistente.setEntidadFinanciera(pagoTransferenciaDto.getEntidadFinanciera());
        }

        pagosTransferenciaRepository.save(pagoExistente);

        return ResponseEntity.ok("Pago por transferencia actualizado");
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> eliminarPagoTransferencia(Integer idTransferencia) {
        if (!pagosTransferenciaRepository.existsById(idTransferencia)) {
            throw new ObjetosNoEncontradosExepcion(mensajePagoNoEncontrado(idTransferencia));
        }

        pagosTransferenciaRepository.deleteById(idTransferencia);

        return ResponseEntity.ok("Pago por transferencia eliminado");
    }

    private String mensajePagoNoEncontrado(Integer idTransferencia) {
        return "El pago por transferencia con ID " + idTransferencia + " no existe";
    }
}
