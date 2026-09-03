package com.entysoftware.aplication.service.interfaces;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.CostosDto;

public interface CostosInterface {

    public ResponseEntity<String> crearCosto(CostosDto costo);
}
