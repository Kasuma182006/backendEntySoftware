package com.entysoftware.aplication.service.service_Implement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.entysoftware.aplication.mapper.MapperInventarioDto;
import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.repository.InventarioRepository;
import com.entysoftware.aplication.service.InventarioInterface;

@Service
public class InventarioServiceImp implements InventarioInterface {

    private final InventarioRepository inventarioRepository;

    private final MapperInventarioDto mapperInventarioDto;

    public InventarioServiceImp(InventarioRepository inventarioRepository, MapperInventarioDto mapperInventarioDto) {
        this.inventarioRepository = inventarioRepository;
        this.mapperInventarioDto = mapperInventarioDto;
    }

    public ResponseEntity<List<InventarioDto>> listarProductos(Integer idEstablecimiento) {
        List<Inventario> listaProductos = inventarioRepository.findByFK_id_establecimiento(idEstablecimiento);
        List<InventarioDto> listaProductosDto = listaProductos.stream()
                                                                .map(mapperInventarioDto::InventarioToDto)
                                                                .toList();

        return ResponseEntity.ok(listaProductosDto);
    }
}
