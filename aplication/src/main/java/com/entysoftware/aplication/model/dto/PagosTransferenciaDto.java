package com.entysoftware.aplication.model.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Pago por transferencia de un establecimiento. Se usa para listar, consultar, crear y editar (edición parcial: los campos nulos se conservan).")
public class PagosTransferenciaDto {

    @Schema(description = "Identificador del pago por transferencia. No se envía al crear; es obligatorio al editar para identificar el registro.", example = "12")
    private Integer idTransferencia;

    @Schema(description = "Identificador del establecimiento que recibió la transferencia. Requerido al crear; no se modifica al editar.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Código o número de referencia de la transferencia. Requerido al crear.", example = "TRX-000123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @Schema(description = "Valor de la transferencia, en la moneda local. Requerido al crear.", example = "45000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer valor;

    @Schema(description = "Entidad financiera desde la que se realizó la transferencia. Requerido al crear.", example = "NEQUI", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entidadFinanciera;

    @Schema(description = "Fecha y hora de registro de la transferencia. La asigna el backend al crear; se ignora si se envía.", example = "2026-09-12T14:32:05", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecha;
}
