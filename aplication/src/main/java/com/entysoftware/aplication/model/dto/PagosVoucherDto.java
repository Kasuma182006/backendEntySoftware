package com.entysoftware.aplication.model.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Pago con voucher de tarjeta de un establecimiento. Se usa para listar, crear y editar (edición parcial: los campos nulos se conservan).")
public class PagosVoucherDto {

    public static final String FORMATO_FECHA = "yyyy-MM-dd HH:mm";

    @Schema(description = "Identificador del pago con voucher. No se envía al crear; es obligatorio al editar para identificar el registro.", example = "8")
    private Integer idVoucher;

    @Schema(description = "Identificador del establecimiento que recibió el pago. Requerido al crear; no se modifica al editar.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @Schema(description = "Código del voucher de la tarjeta. Requerido al crear.", example = "VCH-004512", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @Schema(description = "Valor del pago con voucher, en la moneda local. Requerido al crear.", example = "45000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer valor;

    @JsonFormat(pattern = FORMATO_FECHA)
    @Schema(description = "Fecha, hora y minutos de registro del voucher (sin segundos). La asigna el backend al crear; se ignora si se envía.", type = "string", pattern = "yyyy-MM-dd HH:mm", example = "2026-09-12 14:30", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecha;
}
