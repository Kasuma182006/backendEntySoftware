package com.entysoftware.aplication.model.dto.pedidosDTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Adición solicitada sobre una línea de producto del pedido.
 * Se usa tanto en el request (crear/editar) como en el response (pedidosHoy).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Adición aplicada a un producto del pedido y su cantidad.")
public class AdicionPedidoDto {

    @Schema(description = "Identificador de la adición del catálogo.", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idAdicion;

    @Schema(description = "Nombre de la adición. Se completa a partir del catálogo; se ignora al crear/editar.", example = "Queso extra", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombre;

    @Schema(description = "Cantidad de unidades de la adición.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;
}
