package com.entysoftware.aplication.controller.controllerAdviceDto;


import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Estructura estandarizada de respuesta de error devuelta por el manejador global de excepciones (ControllerAdvice) de la API.")
public class ControllerAdviceDto {

    @Schema(description = "Fecha y hora exacta en la que ocurrió el error.", example = "2026-08-10T14:32:05.123", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime time;

    @Schema(description = "Código de estado HTTP devuelto, expresado como texto.", example = "404", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    @Schema(description = "Nombre corto del error HTTP asociado al código de estado.", example = "Not Found", requiredMode = Schema.RequiredMode.REQUIRED)
    private String error;

    @Schema(description = "Mensaje descriptivo del motivo del error, orientado al consumidor de la API.", example = "No se ha encontrado el ID del establecimiento", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;

    @Schema(description = "Ruta (URI) del recurso que originó el error.", example = "/login", requiredMode = Schema.RequiredMode.REQUIRED)
    private String path;
}
