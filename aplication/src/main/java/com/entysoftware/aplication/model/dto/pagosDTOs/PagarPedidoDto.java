package com.entysoftware.aplication.model.dto.pagosDTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Datos necesarios para registrar el pago de un pedido existente.")
public class PagarPedidoDto {

    @Schema(description = "Identificador del pedido que se está pagando.", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idPedido;

    @Schema(description = "Valor entregado por el cliente para pagar el pedido. Debe ser mayor o igual al valor total del pedido.", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pagoPedido;

    @Schema(description = "Método de pago utilizado.", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoPago;



}
