package com.entysoftware.aplication.service.services;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.error.DatosDuplicados;
import com.entysoftware.aplication.error.ObjetosNoEncontradosExepcion;
import com.entysoftware.aplication.mapper.MapperCierreCajaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.repository.CierreCajaRepository;
import com.entysoftware.aplication.repository.EstablecimientoRepository;
import com.entysoftware.aplication.service.interfaces.CierreCajaInterface;

import jakarta.transaction.Transactional;

@Service
public class CierreCajaServiceImp implements CierreCajaInterface {

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
}
