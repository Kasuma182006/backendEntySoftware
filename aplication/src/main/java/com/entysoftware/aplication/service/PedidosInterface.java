package com.entysoftware.aplication.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;

public interface PedidosInterface {
    
    public ResponseEntity<Integer> crearPedido(PedidosDto pedido);
    
    public ResponseEntity<List<PedidosDto>> pedidosHoy (Integer idEstablecimiento);
    
    public ResponseEntity<String> editarPedido(PedidosDto editarPedido);
    
    public ResponseEntity<FacturaPedidoDto> pagoPedido(PagarPedidoDto pago);

}
