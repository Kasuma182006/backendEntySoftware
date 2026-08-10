package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Producto con mayor cantidad de unidades vendidas dentro de un periodo evaluado.")
public class ProductoMasVendidoDto {

    @Schema(description = "Nombre del producto más vendido.", example = "Hamburguesa clásica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreProducto;

    @Schema(description = "Cantidad total de unidades vendidas del producto en el periodo evaluado.", example = "145", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long cantidadVendida;
}
