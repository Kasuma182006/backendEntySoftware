package com.entysoftware.aplication.model.dto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Establecimiento asociado a una identificación (propietario o empleado), devuelto en la búsqueda previa al login.")
public class EstablecimientosDto {

    @Schema(description = "Nombre comercial del establecimiento.", example = "Restaurante El Buen Sabor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre_establecimiento;

    @Schema(description = "Identificador único del establecimiento.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id_establecimiento;
}
