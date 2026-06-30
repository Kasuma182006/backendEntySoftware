package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.model.dto.pagosDTOs.FacturaPedidoDto;
import com.entysoftware.aplication.model.dto.pagosDTOs.PagarPedidoDto;
import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.service.PedidosInterface;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    
    private final PedidosInterface pedidosInterface;

    public PedidosController(PedidosInterface pedidosInterface){
        this.pedidosInterface = pedidosInterface;
    }

    @PostMapping("/crear-pedido")
    public ResponseEntity<Integer> crearPedido(@RequestBody PedidosDto pedido) {
        
        
        return pedidosInterface.crearPedido(pedido);
    }


    @GetMapping("/pedidos-hoy/{idEstablecimiento}")
    public ResponseEntity<List<PedidosDto>> pedidosHoy(@PathVariable("idEstablecimiento")Integer idEstablecimiento){
        return pedidosInterface.pedidosHoy(idEstablecimiento);
    }
    

    @PatchMapping("/editar-pedido")
    public ResponseEntity<String> editarPedido(@RequestBody PedidosDto Editarpedido){
        return pedidosInterface.editarPedido(Editarpedido);
    }
    
    @PostMapping("/pagar-pedido")
    public ResponseEntity<FacturaPedidoDto> pagarPedido(@RequestBody PagarPedidoDto pagoPedido) {
        
        
        return pedidosInterface.pagoPedido(pagoPedido);
    }
    
    
}
