package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Estado de un establecimiento, consultado desde la gestión de caja.")
public class EstablecimientoEstadoCajaDto {

    @Schema(description = "Identificador único del establecimiento.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idEstablecimiento;

    @Schema(description = "Nombre comercial del establecimiento.", example = "Restaurante El Buen Sabor", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreEstablecimiento;

    @Schema(description = "Estado actual del establecimiento.", example = "abierto", accessMode = Schema.AccessMode.READ_ONLY)
    private String estadoEstablecimiento;
}
