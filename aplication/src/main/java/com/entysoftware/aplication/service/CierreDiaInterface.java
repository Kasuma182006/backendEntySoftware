package com.entysoftware.aplication.service;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.cierreCaja.CierreDiaDto;

public interface CierreDiaInterface {

    public ResponseEntity<CierreDiaDto> cierreDia(Integer idEstablecimiento);
}
