package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Costo (egreso) de un establecimiento. Se usa para registrar un nuevo costo; la fecha la asigna el backend.")
public class CostosDto {

    @Schema(description = "Identificador del establecimiento al que se le registra el costo. Requerido.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Valor del costo, en la moneda local. Requerido.", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer valorCosto;

    @Schema(description = "Método de pago con el que se cubrió el costo.", example = "EFECTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoPago;
}
