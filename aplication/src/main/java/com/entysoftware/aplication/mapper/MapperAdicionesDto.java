package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.AdicionesDto;
import com.entysoftware.aplication.model.models.Adicion;
import com.entysoftware.aplication.model.models.Establecimiento;

@Component
public class MapperAdicionesDto {
    public AdicionesDto adicionesToDto(Adicion adicion) {
        return new AdicionesDto(
            adicion.getIdAdicion(),
            adicion.getIdEstablecimiento(),
            adicion.getNombre(),
            adicion.getPrecioAdicion()
        );
    }

    public Adicion dtoToAdiciones(AdicionesDto adicionDto, Establecimiento establecimiento) {
        Adicion adicion = new Adicion();
        adicion.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        adicion.setNombre(adicionDto.getNombre());
        adicion.setPrecioAdicion(adicionDto.getPrecioAdicion());
        return adicion;
    }
}
