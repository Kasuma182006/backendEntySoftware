package com.entysoftware.aplication.service.interfaces;


import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.model.dto.PaginaDto;

public interface InventarioInterface {

    public ResponseEntity<PaginaDto<InventarioDto>> listarProductos(Integer idEstablecimiento, int pagina, int tamano);

    public ResponseEntity<InventarioDto> crearProducto(InventarioDto producto);

    public ResponseEntity<String> editarProducto(InventarioDto producto);
}
