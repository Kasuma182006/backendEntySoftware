package com.entysoftware.aplication.model.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PedidosDto {
    
    private int FK_id_mesa;
    private int valor_domicilio;
    private int precio_total;
    private String descripcion;
    private List<Map<String,Integer>> pedido;

}
