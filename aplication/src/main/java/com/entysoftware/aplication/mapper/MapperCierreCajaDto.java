package com.entysoftware.aplication.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;
import com.entysoftware.aplication.model.models.CierreCaja;
import com.entysoftware.aplication.model.models.Establecimiento;

@Component
public class MapperCierreCajaDto {

    public CuadreCajaDto cierreCajaToDto(CierreCaja cierreCaja) {
        return new CuadreCajaDto(
            cierreCaja.getId(),
            cierreCaja.getIdEstablecimiento(),
            cierreCaja.getCantidadTransferencia(),
            cierreCaja.getCantidadEfectivo(),
            cierreCaja.getCantidadTarjeta(),
            cierreCaja.getFecha()
        );
    }

    public CierreCaja dtoToCierreCaja(CuadreCajaDto cuadreCajaDto, Establecimiento establecimiento) {
        CierreCaja cierreCaja = new CierreCaja();
        cierreCaja.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        cierreCaja.setCantidadTransferencia(cuadreCajaDto.getCantidadTransferencia());
        cierreCaja.setCantidadEfectivo(cuadreCajaDto.getCantidadEfectivo());
        cierreCaja.setCantidadTarjeta(cuadreCajaDto.getCantidadTarjeta());
        cierreCaja.setFecha(LocalDateTime.now());
        return cierreCaja;
    }
}
