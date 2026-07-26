package com.entysoftware.aplication.service;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.baseDia.BaseInicialDto;
import com.entysoftware.aplication.model.dto.baseDia.RespuestaBaseInicialDto;

public interface CajaInterface {
    public ResponseEntity<RespuestaBaseInicialDto> aperturaDia(BaseInicialDto base);
}
