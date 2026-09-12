package com.entysoftware.aplication.service.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperPagosVoucherDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.PagosVoucherDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoVoucher;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.repository.PagosVoucherRepository;
import com.entysoftware.aplication.service.interfaces.PagosVoucherInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;
import com.entysoftware.aplication.utils.RangoFechas;

import jakarta.transaction.Transactional;

@Service
public class PagosVoucherServiceImp implements PagosVoucherInterface {

    private static final String CAMPO_ORDEN = "fecha";

    private final PagosVoucherRepository pagosVoucherRepository;

    private final MapperPagosVoucherDto mapperPagosVoucherDto;

    private final EstablecimientoRepository establecimientoRepository;

    public PagosVoucherServiceImp(PagosVoucherRepository pagosVoucherRepository, MapperPagosVoucherDto mapperPagosVoucherDto,
            EstablecimientoRepository establecimientoRepository) {
        this.pagosVoucherRepository = pagosVoucherRepository;
        this.mapperPagosVoucherDto = mapperPagosVoucherDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    /**
     * Lista paginada de vouchers de un establecimiento, del más reciente al más antiguo.
     * Sin fechas se listan todos; con una sola fecha se filtra ese día; con ambas se filtra el rango (inclusivo).
     */
    public ResponseEntity<PaginaDto<PagosVoucherDto>> listarPagosVoucher(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoDesc(pagina, tamano, CAMPO_ORDEN);

        Page<PagoVoucher> paginaPagos = RangoFechas.resolver(fechaInicio, fechaFin)
            .map(rango -> pagosVoucherRepository.buscarPorEstablecimientoYRangoFechas(idEstablecimiento, rango.desde(), rango.hasta(), pageable))
            .orElseGet(() -> pagosVoucherRepository.findByIdEstablecimiento(idEstablecimiento, pageable));

        return ResponseEntity.ok(PaginaDto.desdePage(paginaPagos.map(mapperPagosVoucherDto::pagoVoucherToDto)));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<PagosVoucherDto> crearPagoVoucher(PagosVoucherDto pagoVoucherDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(pagoVoucherDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + pagoVoucherDto.getIdEstablecimiento() + " no existe"));

        PagoVoucher pagoVoucher = mapperPagosVoucherDto.dtoToPagoVoucher(pagoVoucherDto, establecimiento);
        PagoVoucher pagoGuardado = pagosVoucherRepository.save(pagoVoucher);

        return ResponseEntity.ok(mapperPagosVoucherDto.pagoVoucherToDto(pagoGuardado));
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> editarPagoVoucher(PagosVoucherDto pagoVoucherDto) {
        PagoVoucher pagoExistente = pagosVoucherRepository.findById(pagoVoucherDto.getIdVoucher())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion(mensajePagoNoEncontrado(pagoVoucherDto.getIdVoucher())));

        if (pagoVoucherDto.getCodigo() != null) {
            pagoExistente.setCodigo(pagoVoucherDto.getCodigo());
        }
        if (pagoVoucherDto.getValor() != null) {
            pagoExistente.setValor(pagoVoucherDto.getValor());
        }

        pagosVoucherRepository.save(pagoExistente);

        return ResponseEntity.ok("Pago con voucher actualizado");
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<String> eliminarPagoVoucher(Integer idVoucher) {
        if (!pagosVoucherRepository.existsById(idVoucher)) {
            throw new ObjetosNoEncontradosExepcion(mensajePagoNoEncontrado(idVoucher));
        }

        pagosVoucherRepository.deleteById(idVoucher);

        return ResponseEntity.ok("Pago con voucher eliminado");
    }

    private String mensajePagoNoEncontrado(Integer idVoucher) {
        return "El pago con voucher con ID " + idVoucher + " no existe";
    }
}
