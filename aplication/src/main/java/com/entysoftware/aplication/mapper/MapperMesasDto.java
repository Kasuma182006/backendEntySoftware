package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.model.models.Establecimiento;
import com.entysoftware.aplication.model.models.Mesa;
@Component
public class MapperMesasDto {
    public MesasDto MesasToDto( Mesa mesa){
        return new MesasDto(mesa.getIdMesa(), mesa.getIdEstablecimiento(), mesa.getNombreMesa(), mesa.getEstadoMesa());
    }

    public Mesa dtoToMesas(MesasDto mesaDto, Establecimiento establecimiento){
        Mesa mesa = new Mesa();
        mesa.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        mesa.setNombreMesa(mesaDto.getNombreMesa());
        mesa.setEstadoMesa(mesaDto.getOcupada());
        return mesa;
    }
}
