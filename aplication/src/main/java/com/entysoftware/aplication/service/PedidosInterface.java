package com.entysoftware.aplication.service;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.PedidosDto;

public interface PedidosInterface {
    
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido);
}
