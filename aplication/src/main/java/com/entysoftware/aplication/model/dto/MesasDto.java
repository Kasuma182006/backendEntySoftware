package com.entysoftware.aplication.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Mesa de un establecimiento. Se usa tanto para crear una mesa como para editarla (edición parcial: los campos nulos se conservan).")
public class MesasDto {

    @Schema(description = "Identificador de la mesa. No se envía al crear una mesa; es obligatorio al editarla.", example = "3")
    private Integer idMesa;

    @Schema(description = "Identificador del establecimiento al que pertenece la mesa. Requerido al crear una mesa.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Nombre o número identificador de la mesa.", example = "Mesa 5", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombreMesa;

    @Schema(description = "Indica si la mesa está ocupada (true) o libre (false).", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean estadoMesa;


}
