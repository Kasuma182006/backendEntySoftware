package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.MesasDto;
@Component
public class MapperMesasDto {
    public MesasDto MesasToDto( Mesas mesa){
        return new MesasDto(mesa.getIdMesa(), mesa.getNombreMesa(),mesa.getEstadoMesa());
    } 
}
