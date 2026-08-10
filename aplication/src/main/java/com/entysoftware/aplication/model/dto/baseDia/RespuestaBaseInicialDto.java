package com.entysoftware.aplication.model.dto.baseDia;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(description = "Confirmación del registro de apertura de caja (base inicial) del día.")
public class RespuestaBaseInicialDto {

    @Schema(description = "Indica si la caja del establecimiento quedó abierta tras el registro.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean abierto;

    @Schema(description = "Valor monetario con el que se abrió la caja del día.", example = "50000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer baseInicial;

    @Schema(description = "Hora en la que se registró la apertura de caja, truncada al minuto.", example = "08:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horaApertura;

    @Schema(description = "Fecha en la que se registró la apertura de caja.", example = "2026-08-10", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fecha;
}
