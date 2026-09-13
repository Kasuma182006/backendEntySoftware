package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Categoría del catálogo de un establecimiento. Se usa para listar, crear y editar (edición parcial: los campos nulos se conservan).")
public class CategoriasDto {

    @Schema(description = "Identificador de la categoría. No se envía al crear; es obligatorio al editar para identificar el registro.", example = "2")
    private Integer idCategoria;

    @Schema(description = "Identificador del establecimiento dueño de la categoría. Requerido al crear; no se modifica al editar.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Nombre de la categoría. Requerido al crear.", example = "Bebidas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;
}
