package com.entysoftware.aplication.model.dto.cierreCaja;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Desempeño de ventas de un producto del inventario durante el mes.")
public class ProductoMes {

    @Schema(description = "Identificador del producto en el inventario.", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "Nombre del producto.", example = "Hamburguesa clásica", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Cantidad de unidades vendidas del producto en el mes.", example = "145", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadUnidadesVendidas;

    @Schema(description = "Ingresos totales generados por el producto en el mes, en la moneda local.", example = "2175000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadIngresos;
}
