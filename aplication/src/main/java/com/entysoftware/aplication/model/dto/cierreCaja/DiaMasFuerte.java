package com.entysoftware.aplication.model.dto.cierreCaja;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Día con mayor volumen de ventas dentro de un periodo (por ejemplo, un mes) para un establecimiento.")
public class DiaMasFuerte {

    @Schema(description = "Fecha del día con mayor volumen de ventas.", example = "2026-08-08", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dia;

    @Schema(description = "Cantidad total de productos vendidos ese día.", example = "58", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadProductosVendidos;

    @Schema(description = "Valor total vendido ese día, en la moneda local.", example = "1250000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalVendido;
}
