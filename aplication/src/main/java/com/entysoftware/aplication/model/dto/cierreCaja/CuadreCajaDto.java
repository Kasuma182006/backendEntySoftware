package com.entysoftware.aplication.model.dto.cierreCaja;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Cuadre de caja de un establecimiento: cantidades contadas por cada método de pago al cierre del día.")
public class CuadreCajaDto {

    @Schema(description = "Identificador del cuadre de caja. No se envía al crear; lo asigna el backend.", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idCierreCaja;

    @NotNull(message = "El campo idEstablecimiento es obligatorio")
    @Schema(description = "Identificador del establecimiento al que pertenece el cuadre de caja. Requerido.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer idEstablecimiento;

    @NotNull(message = "El campo cantidadTransferencia es obligatorio")
    @PositiveOrZero(message = "El campo cantidadTransferencia no puede ser un valor negativo")
    @Schema(description = "Cantidad contada en transferencias. No puede ser negativa.", example = "450000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadTransferencia;

    @NotNull(message = "El campo cantidadEfectivo es obligatorio")
    @PositiveOrZero(message = "El campo cantidadEfectivo no puede ser un valor negativo")
    @Schema(description = "Cantidad contada en efectivo. No puede ser negativa.", example = "320000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadEfectivo;

    @NotNull(message = "El campo cantidadTarjeta es obligatorio")
    @PositiveOrZero(message = "El campo cantidadTarjeta no puede ser un valor negativo")
    @Schema(description = "Cantidad contada en tarjeta. No puede ser negativa.", example = "150000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidadTarjeta;

    @Schema(description = "Fecha y hora en que se registró el cuadre de caja. La asigna el backend; se ignora si se envía.", example = "2026-09-16T18:05:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecha;
}
