package com.entysoftware.aplication.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.MesasDto;

public interface MesasInterface {

    public ResponseEntity<List<MesasDto>> listarMesas(Integer idEstablecimiento);

    public ResponseEntity<MesasDto> crearMesa(MesasDto mesa);

    public ResponseEntity<String> editarMesa(MesasDto mesa);

    public ResponseEntity<String> eliminarMesa(Integer idMesa);
}
