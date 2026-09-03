package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.Mesas;
@Component
public class MapperMesasDto {
    public MesasDto MesasToDto( Mesas mesa){
        return new MesasDto(mesa.getIdMesa(), mesa.getIdEstablecimiento(), mesa.getNombreMesa(), mesa.getEstadoMesa());
    }

    public Mesas dtoToMesas(MesasDto mesaDto, Establecimiento establecimiento){
        Mesas mesa = new Mesas();
        mesa.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        mesa.setNombreMesa(mesaDto.getNombreMesa());
        mesa.setEstadoMesa(mesaDto.getOcupada());
        return mesa;
    }
}
