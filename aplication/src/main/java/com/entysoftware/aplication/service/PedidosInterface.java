package com.entysoftware.aplication.service;

import java.util.List;

import org.springframework.http.ResponseEntity;


import com.entysoftware.aplication.model.dto.PedidosDto;

public interface PedidosInterface {
    
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido);
    
    public ResponseEntity<List<PedidosDto>> pedidosHoy (Integer idEstablecimiento);
    
    public ResponseEntity<String> editarPedido(PedidosDto editarPedido);
}
