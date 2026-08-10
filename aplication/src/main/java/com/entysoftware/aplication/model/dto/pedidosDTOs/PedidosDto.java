package com.entysoftware.aplication.model.dto.pedidosDTOs;

import java.time.LocalDate;
import java.util.List;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Pedido realizado en una mesa: encabezado con montos y estado, y el detalle de productos solicitados.")
public class PedidosDto {

    @Schema(description = "Identificador del pedido. Generado por el sistema; se ignora al crear un pedido.", example = "45", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idPedido;

    @Schema(description = "Identificador de la mesa a la que pertenece el pedido.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idMesa;

    @Schema(description = "Método de pago del pedido. Se establece al pagar el pedido; se ignora al crearlo.", example = "EFECTIVO", allowableValues = {"EFECTIVO", "TRANSFERENCIA"}, accessMode = Schema.AccessMode.READ_ONLY)
    private String tipoPago;

    @Schema(description = "Estado actual del pedido. Gestionado por el sistema; se ignora al crearlo.", example = "EN ESPERA", allowableValues = {"EN ESPERA", "PAGO"}, accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoPedido;

    @Schema(description = "Valor del domicilio asociado al pedido, si aplica.", example = "5000")
    private Integer valorDomicilio;

    @Schema(description = "Precio total del pedido, sumando todos los productos solicitados.", example = "48000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer precioTotal;

    @Schema(description = "Fecha en la que se realizó el pedido. Generada por el sistema; se ignora al crearlo.", example = "2026-08-10", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaPedido;

    @Schema(description = "Descripción u observaciones adicionales del pedido.", example = "Sin cebolla, para llevar")
    private String descripcion;

    @Schema(description = "Listado de productos solicitados en el pedido.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DetallesPedidoDto> pedido;


}
