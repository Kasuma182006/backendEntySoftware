package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.Establecimiento;
import com.entysoftware.aplication.model.dto.EstablecimientoEstadoCajaDto;

@Component
public class MapperEstablecimientoDto {
    public EstablecimientoEstadoCajaDto establecimientoToEstadoCajaDto(Establecimiento establecimiento) {
        return new EstablecimientoEstadoCajaDto(
            establecimiento.getIdEstablecimiento(),
            establecimiento.getNombreEstablecimiento(),
            establecimiento.getEstadoEstablecimiento()
        );
    }
}
