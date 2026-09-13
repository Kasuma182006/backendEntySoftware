package com.entysoftware.aplication.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.CategoriasDto;
import com.entysoftware.aplication.model.dto.PaginaDto;

public interface CategoriasInterface {

    public ResponseEntity<PaginaDto<CategoriasDto>> listarCategorias(Integer idEstablecimiento, String nombre, int pagina, int tamano);

    public ResponseEntity<CategoriasDto> crearCategoria(CategoriasDto categoria);

    public ResponseEntity<String> editarCategoria(CategoriasDto categoria);
}
