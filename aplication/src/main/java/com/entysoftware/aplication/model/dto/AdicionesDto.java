package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Adición del catálogo de un establecimiento. Se usa para listar, crear y editar (edición parcial: los campos nulos se conservan).")
public class AdicionesDto {

    @Schema(description = "Identificador de la adición. No se envía al crear; es obligatorio al editar para identificar el registro.", example = "7")
    private Integer idAdicion;

    @Schema(description = "Identificador del establecimiento dueño de la adición. Requerido al crear.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Nombre de la adición. Requerido al crear.", example = "Queso extra", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Precio de la adición, en la moneda local. Requerido al crear.", example = "2000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer precioAdicion;
}
