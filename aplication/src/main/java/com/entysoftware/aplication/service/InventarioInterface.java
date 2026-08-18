package com.entysoftware.aplication.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.InventarioDto;

public interface InventarioInterface {

    public ResponseEntity<List<InventarioDto>> listarProductos(Integer idEstablecimiento);
}
