package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.PedidosDto;
import com.entysoftware.aplication.service.PedidosInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    
    private final PedidosInterface pedidosInterface;

    public PedidosController(PedidosInterface pedidosInterface){
        this.pedidosInterface = pedidosInterface;
    }

    @PostMapping("/crear-pedido")
    public ResponseEntity<?> crearPedido(@RequestBody PedidosDto pedido) {
        
        
        return pedidosInterface.crearPedido(pedido);
    }
    
}
