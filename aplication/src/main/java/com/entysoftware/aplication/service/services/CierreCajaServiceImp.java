package com.entysoftware.aplication.service.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.error.RangoFechasInvalidoException;
import com.entysoftware.aplication.mapper.MapperCierreCajaDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.ReporteCierreCaja;
import com.entysoftware.aplication.model.models.BaseInicial;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.BaseInicialRepository;
import com.entysoftware.aplication.repository.CierreCajaRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CierreCajaInterface;
import com.entysoftware.aplication.service.interfaces.CierreDiaInterface;
import com.entysoftware.aplication.utils.ConsultaPaginadaUtils;
import com.entysoftware.aplication.utils.GeneradorPdfCierreCaja;
import com.entysoftware.aplication.utils.RangoFechas;

import jakarta.transaction.Transactional;

@Service
public class CierreCajaServiceImp implements CierreCajaInterface {

    private static final String CAMPO_ORDEN = "fecha";
    private static final String ESTADO_ESTABLECIMIENTO_CERRADO = "CERRADO";

    private final CierreCajaRepository cierreCajaRepository;

    private final MapperCierreCajaDto mapperCierreCajaDto;

    private final EstablecimientoRepository establecimientoRepository;

    private final BaseInicialRepository baseInicialRepository;

    private final CierreDiaInterface cierreDiaInterface;

    private final GeneradorPdfCierreCaja generadorPdfCierreCaja;

    public CierreCajaServiceImp(CierreCajaRepository cierreCajaRepository, MapperCierreCajaDto mapperCierreCajaDto,
            EstablecimientoRepository establecimientoRepository, BaseInicialRepository baseInicialRepository,
            CierreDiaInterface cierreDiaInterface, GeneradorPdfCierreCaja generadorPdfCierreCaja) {
        this.cierreCajaRepository = cierreCajaRepository;
        this.mapperCierreCajaDto = mapperCierreCajaDto;
        this.establecimientoRepository = establecimientoRepository;
        this.baseInicialRepository = baseInicialRepository;
        this.cierreDiaInterface = cierreDiaInterface;
        this.generadorPdfCierreCaja = generadorPdfCierreCaja;
    }

    /**
     * Registra el cuadre de caja, cierra el establecimiento y devuelve el PDF del cierre.
     * Si el PDF no se puede generar, la transacción se revierte y el día no queda cerrado.
     */
    @SuppressWarnings("null")
    @Transactional
    public ResponseEntity<byte[]> cuadreCaja(CuadreCajaDto cuadreCajaDto) {
        Establecimiento establecimiento = establecimientoRepository.findById(cuadreCajaDto.getIdEstablecimiento())
            .orElseThrow(() -> new ObjetosNoEncontradosExepcion("El establecimiento con ID " + cuadreCajaDto.getIdEstablecimiento() + " no existe"));

        LocalDate hoy = LocalDate.now();
        if (cierreCajaRepository.existsPorEstablecimientoYFecha(establecimiento.getIdEstablecimiento(), hoy)) {
            throw new DatosDuplicados("Ya existe un cuadre de caja para el establecimiento con ID " + establecimiento.getIdEstablecimiento() + " en la fecha " + hoy);
        }

        // Se calcula antes de guardar: si falta la base inicial del día, falla aquí sin registrar el cuadre
        CierreDiaDto cierreDia = cierreDiaInterface.cierreDia(establecimiento.getIdEstablecimiento()).getBody();

        CierreCaja cierreCaja = mapperCierreCajaDto.dtoToCierreCaja(cuadreCajaDto, establecimiento);
        CierreCaja cierreCajaGuardado = cierreCajaRepository.save(cierreCaja);
        establecimientoRepository.actualizarEstadoEstablecimiento(ESTADO_ESTABLECIMIENTO_CERRADO, establecimiento.getIdEstablecimiento()); // El cuadre de caja es el que cierra el establecimiento

        byte[] pdf = generadorPdfCierreCaja.generar(construirReporte(establecimiento, cierreCajaGuardado, cierreDia, hoy));
        String nombreArchivo = "cierre-caja-" + establecimiento.getIdEstablecimiento() + "-" + hoy + ".pdf";

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(nombreArchivo).build().toString())
            .body(pdf);
    }

    private ReporteCierreCaja construirReporte(Establecimiento establecimiento, CierreCaja cierreCaja, CierreDiaDto cierreDia, LocalDate hoy) {
        BaseInicial baseInicial = baseInicialRepository.validarExistenciaBaseInicial(establecimiento.getIdEstablecimiento(), hoy);

        return new ReporteCierreCaja(
            cierreCaja.getId(),
            establecimiento.getNombreEstablecimiento(),
            cierreCaja.getFecha(),
            baseInicial != null ? baseInicial.getHora() : null,
            cierreDia,
            cierreCaja.getCantidadEfectivo(),
            cierreCaja.getCantidadTransferencia(),
            cierreCaja.getCantidadTarjeta()
        );
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
