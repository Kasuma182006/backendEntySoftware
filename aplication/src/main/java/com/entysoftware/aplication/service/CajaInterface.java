package com.entysoftware.aplication.service;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.BaseInicialDto;

public interface CajaInterface {
    public ResponseEntity<String> aperturaDia(BaseInicialDto base);
}
