package com.entysoftware.aplication.model.dto.pagosDTOs;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagarPedidoDto {
    private Integer idPedido; 
    private int pagoPedido;
    private String tipoPago;



}
