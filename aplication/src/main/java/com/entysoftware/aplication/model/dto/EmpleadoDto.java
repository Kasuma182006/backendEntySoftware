package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Empleado de un establecimiento. No incluye el identificador ni la contraseña del empleado.")
public class EmpleadoDto {

    @Schema(description = "Identificador del establecimiento al que pertenece el empleado.", example = "1")
    private Integer idEstablecimiento;

    @Schema(description = "Número de identificación del empleado.", example = "1020304050")
    private String numeroIdentificacion;

    @Schema(description = "Nombre del empleado.", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Rol del empleado dentro del establecimiento.", example = "ASISTENTE")
    private String rol;
}
