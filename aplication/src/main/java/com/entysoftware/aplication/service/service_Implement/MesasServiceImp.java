package com.entysoftware.aplication.service.service_Implement;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.mapper.MapperMesasDto;
import com.entysoftware.aplication.model.Mesas;
import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.repository.MesasRepository;

@Service
public class MesasServiceImp {

    
    private MesasRepository mesasRepository;
    
    private MapperMesasDto mapperMesasDto;

    public MesasServiceImp(MesasRepository mesasRepository, MapperMesasDto mapperMesasDto){
        this.mapperMesasDto = mapperMesasDto;
        this.mesasRepository = mesasRepository;
    }

    public ResponseEntity<List<MesasDto>> listarMesas(Integer idEstablecimiento){
        List<Mesas> listaMesas = mesasRepository.findByFK_id_establecimiento(idEstablecimiento);
        List<MesasDto> listaMesasDto = listaMesas.stream()
                                                  .map(mapperMesasDto::MesasToDto)
                                                  .toList();
        
        return ResponseEntity.ok(listaMesasDto);
    }
    
}
