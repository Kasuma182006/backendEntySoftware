package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.dto.InventarioDto;

@Component
public class MapperInventarioDto {
    public InventarioDto InventarioToDto(Inventario inventario) {
        return new InventarioDto(
            inventario.getIdInventario(),
            inventario.getNombre(),
            inventario.getCategoria(),
            inventario.getDescripcion(),
            inventario.getPrecio()
        );
    }
}
