package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.CategoriasDto;
import com.entysoftware.aplication.model.models.Categoria;
import com.entysoftware.aplication.model.models.Establecimiento;

@Component
public class MapperCategoriasDto {

    public CategoriasDto categoriaToDto(Categoria categoria) {
        return new CategoriasDto(
            categoria.getId(),
            categoria.getIdEstablecimiento(),
            categoria.getNombre()
        );
    }

    public Categoria dtoToCategoria(CategoriasDto categoriaDto, Establecimiento establecimiento) {
        Categoria categoria = new Categoria();
        categoria.setIdEstablecimiento(establecimiento.getIdEstablecimiento());
        categoria.setNombre(categoriaDto.getNombre());
        return categoria;
    }
}
