package com.entysoftware.aplication.service.interfaces;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.AdicionesDto;

public interface AdicionesInterface {

    public ResponseEntity<List<AdicionesDto>> listarAdiciones(Integer idEstablecimiento);

    public ResponseEntity<AdicionesDto> crearAdicion(AdicionesDto adicion);

    public ResponseEntity<String> editarAdicion(AdicionesDto adicion);

   /* public ResponseEntity<String> eliminarAdicion(Integer idAdicion);*/
}
