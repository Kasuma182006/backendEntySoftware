package com.entysoftware.aplication.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.PagosTransferenciaDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoTransferencia;

@Component
public class MapperPagosTransferenciaDto {

    public PagosTransferenciaDto pagoTransferenciaToDto(PagoTransferencia pagoTransferencia) {
        return new PagosTransferenciaDto(
            pagoTransferencia.getIdTransferencia(),
            pagoTransferencia.getIdEstablecimiento(),
            pagoTransferencia.getCodigo(),
            pagoTransferencia.getValor(),
            pagoTransferencia.getEntidadFinanciera(),
            pagoTransferencia.getFecha()
        );
    }

    public PagoTransferencia dtoToPagoTransferencia(PagosTransferenciaDto pagoTransferenciaDto, Establecimiento establecimiento) {
        PagoTransferencia pagoTransferencia = new PagoTransferencia();
        pagoTransferencia.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        pagoTransferencia.setCodigo(pagoTransferenciaDto.getCodigo());
        pagoTransferencia.setValor(pagoTransferenciaDto.getValor());
        pagoTransferencia.setEntidadFinanciera(pagoTransferenciaDto.getEntidadFinanciera());
        pagoTransferencia.setFecha(LocalDateTime.now());
        return pagoTransferencia;
    }
}
