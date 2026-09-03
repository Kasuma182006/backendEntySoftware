package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.AdicionesDto;
import com.entysoftware.aplication.model.models.Adiciones;
import com.entysoftware.aplication.model.models.Establecimiento;

@Component
public class MapperAdicionesDto {
    public AdicionesDto adicionesToDto(Adiciones adicion) {
        return new AdicionesDto(
            adicion.getIdAdicion(),
            adicion.getIdEstablecimiento(),
            adicion.getNombre(),
            adicion.getPrecioAdicion()
        );
    }

    public Adiciones dtoToAdiciones(AdicionesDto adicionDto, Establecimiento establecimiento) {
        Adiciones adicion = new Adiciones();
        adicion.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        adicion.setNombre(adicionDto.getNombre());
        adicion.setPrecioAdicion(adicionDto.getPrecioAdicion());
        return adicion;
    }
}
