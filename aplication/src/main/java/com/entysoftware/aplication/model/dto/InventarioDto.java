package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Producto del inventario. Se usa para listar, crear y editar (edición parcial: los campos nulos se conservan).")
public class InventarioDto {

    @Schema(description = "Identificador del producto en el inventario. No se envía al crear; es obligatorio al editar para identificar el registro.", example = "12")
    private Integer idInventario;

    @Schema(description = "Nombre del producto. Requerido al crear. No puede repetirse dentro de la misma categoría.", example = "Hamburguesa clásica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Identificador de la categoría a la que pertenece el producto. Requerido al crear; al editar permite mover el producto a otra categoría.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer categoria;

    @Schema(description = "Descripción del producto.", example = "Hamburguesa de carne con queso, lechuga y tomate")
    private String descripcion;

    @Schema(description = "Precio de venta del producto, en la moneda local. Requerido al crear.", example = "15000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer precio;
}
