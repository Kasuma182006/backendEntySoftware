package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.model.models.Inventario;

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
