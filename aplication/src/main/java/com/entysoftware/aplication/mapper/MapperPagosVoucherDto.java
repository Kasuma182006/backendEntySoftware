package com.entysoftware.aplication.mapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.PagosVoucherDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.PagoVoucher;

@Component
public class MapperPagosVoucherDto {

    public PagosVoucherDto pagoVoucherToDto(PagoVoucher pagoVoucher) {
        return new PagosVoucherDto(
            pagoVoucher.getIdVoucher(),
            pagoVoucher.getIdEstablecimiento(),
            pagoVoucher.getCodigo(),
            pagoVoucher.getValor(),
            truncarAMinutos(pagoVoucher.getFecha())
        );
    }

    public PagoVoucher dtoToPagoVoucher(PagosVoucherDto pagoVoucherDto, Establecimiento establecimiento) {
        PagoVoucher pagoVoucher = new PagoVoucher();
        pagoVoucher.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        pagoVoucher.setCodigo(pagoVoucherDto.getCodigo());
        pagoVoucher.setValor(pagoVoucherDto.getValor());
        pagoVoucher.setFecha(truncarAMinutos(LocalDateTime.now()));
        return pagoVoucher;
    }

    private LocalDateTime truncarAMinutos(LocalDateTime fecha) {
        return fecha == null ? null : fecha.truncatedTo(ChronoUnit.MINUTES);
    }
}
