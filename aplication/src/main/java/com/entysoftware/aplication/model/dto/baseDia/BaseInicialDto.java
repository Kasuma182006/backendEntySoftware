package com.entysoftware.aplication.model.dto.baseDia;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Datos necesarios para registrar la apertura de caja (base inicial) del día en un establecimiento.")
public class BaseInicialDto {

    @Schema(description = "Identificador del establecimiento donde se registra la apertura de caja.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Valor monetario con el que se abre la caja (base inicial) del día, en la moneda local.", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    private int valor;

}
