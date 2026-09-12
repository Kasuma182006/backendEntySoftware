package com.entysoftware.aplication.mapper;

import org.springframework.stereotype.Component;

import com.entysoftware.aplication.model.dto.pedidosDTOs.PedidosDto;
import com.entysoftware.aplication.model.models.EncabezadoPedido;

@Component
public class MapperPedidosDto {
    public PedidosDto pedidosToEntity(EncabezadoPedido encabezadoPedidos){
        return new PedidosDto(encabezadoPedidos.getIdPedido(),encabezadoPedidos.getIdMesa().getIdMesa(), encabezadoPedidos.getTipoPago(), encabezadoPedidos.getEstadoPedido(), encabezadoPedidos.getValorDomicilio(), encabezadoPedidos.getPrecioTotal(), encabezadoPedidos.getFechaPedido(), encabezadoPedidos.getDescripcion(), null);
    }





}
