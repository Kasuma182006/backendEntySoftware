package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Producto del inventario, expuesto en la capa de transporte hacia el cliente.")
public class InventarioDto {

    @Schema(description = "Identificador del producto en el inventario.", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventario;

    @Schema(description = "Nombre del producto.", example = "Hamburguesa clásica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Identificador de la categoría a la que pertenece el producto.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer categoria;

    @Schema(description = "Descripción del producto.", example = "Hamburguesa de carne con queso, lechuga y tomate")
    private String descripcion;

    @Schema(description = "Precio de venta del producto, en la moneda local.", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer precio;
}
