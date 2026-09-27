package com.entysoftware.aplication.service.interfaces;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.model.dto.cierreCaja.CuadreCajaDto;

public interface CierreCajaInterface {

    public ResponseEntity<CuadreCajaDto> cuadreCaja(CuadreCajaDto cuadreCaja);

    public ResponseEntity<PaginaDto<CuadreCajaDto>> listarCierresCaja(Integer idEstablecimiento, LocalDate fechaInicio,
            LocalDate fechaFin, int pagina, int tamano);
}
