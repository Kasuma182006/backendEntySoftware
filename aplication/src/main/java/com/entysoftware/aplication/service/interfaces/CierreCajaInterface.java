package com.entysoftware.aplication.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;

public interface CierreCajaInterface {

    public ResponseEntity<CuadreCajaDto> cuadreCaja(CuadreCajaDto cuadreCaja);
}
