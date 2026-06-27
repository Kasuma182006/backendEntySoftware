package com.entysoftware.aplication.model.dto.pagosDTOs;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaPedidoDto {
    private Integer idPedido;
    private Integer idMesa; 
    private String estadoPedido;
    private int valorTotal;
    private int valorCambio;
    private String tipoPago;
    private String fechaYHora;
}
