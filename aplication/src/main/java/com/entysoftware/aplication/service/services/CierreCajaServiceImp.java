package com.entysoftware.aplication.service.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.error.RangoFechasInvalidoException;
import com.entysoftware.aplication.mapper.MapperCierreCajaDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.CierreCajaRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CierreCajaInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;
import com.entysoftware.aplication.utils.RangoFechas;

import jakarta.transaction.Transactional;

@Service
public class CierreCajaServiceImp implements CierreCajaInterface {

    private static final String CAMPO_ORDEN = "fecha";

    private final CierreCajaRepository cierreCajaRepository;

    private final MapperCierreCajaDto mapperCierreCajaDto;

    private final EstablecimientoRepository establecimientoRepository;

    public CierreCajaServiceImp(CierreCajaRepository cierreCajaRepository, MapperCierreCajaDto mapperCierreCajaDto,
            EstablecimientoRepository establecimientoRepository) {
        this.cierreCajaRepository = cierreCajaRepository;
        this.mapperCierreCajaDto = mapperCierreCajaDto;
        this.establecimientoRepository = establecimientoRepository;
    }

    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<CuadreCajaDto> cuadreCaja(CuadreCajaDto cuadreCajaDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(cuadreCajaDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + cuadreCajaDto.getIdEstablecimiento() + " no existe"));

        LocalDate hoy = LocalDate.now();
        if (cierreCajaRepository.existsPorEstablecimientoYFecha(establecimiento.getIdEstablecimiento(), hoy)) {
            throw new DatosDuplicados("Ya existe un cuadre de caja para el establecimiento con ID " + establecimiento.getIdEstablecimiento() + " en la fecha " + hoy);
        }

        CierreCaja cierreCaja = mapperCierreCajaDto.dtoToCierreCaja(cuadreCajaDto, establecimiento);
        CierreCaja cierreCajaGuardado = cierreCajaRepository.save(cierreCaja);

        return ResponseEntity.ok(mapperCierreCajaDto.cierreCajaToDto(cierreCajaGuardado));
    }

    /**
     * Lista paginada de cierres de caja de un establecimiento, del más reciente al más antiguo.
     * Con solo fechaInicio se filtra ese día; con ambas fechas se filtra el rango (inclusivo).
     */
    public ResponseEntity<PaginaDto<CuadreCajaDto>> listarCierresCaja(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano) {
        Pageable pageable = ConsultaPaginadaUtils.construirPageableOrdenadoDesc(pagina, tamano, CAMPO_ORDEN);

        RangoFechas rango = RangoFechas.resolver(fechaInicio, fechaFin)
            .orElseThrow(() -> new RangoFechasInvalidoException("Debe indicar al menos la fecha de inicio"));

        Page<CierreCaja> paginaCierres = cierreCajaRepository.buscarPorEstablecimientoYRangoFechas(
            idEstablecimiento, rango.desde(), rango.hasta(), pageable);

        return ResponseEntity.ok(PaginaDto.desdePage(paginaCierres.map(mapperCierreCajaDto::cierreCajaToDto)));
    }
}
