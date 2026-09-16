package com.entysoftware.aplication.model.dto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Resultado de un login exitoso: datos del usuario autenticado, su token de sesión y el estado inicial del establecimiento (mesas, categorías e inventario).")
public class LoginSuccesfulDto {

    @Schema(description = "Número de identificación del usuario autenticado.", example = "1094567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero_identificacion;

    @Schema(description = "Nombre completo del usuario autenticado.", example = "Juan Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Rol del usuario autenticado dentro del establecimiento.", example = "administrador", allowableValues = {"administrador", "empleado"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String rol;

    @Schema(description = "Identificador del establecimiento en el que inició sesión el usuario.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id_establecimiento;

    @Schema(description = "Nombre comercial del establecimiento.", example = "Restaurante El Buen Sabor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre_establecimiento;

    @Schema(description = "Token JWT de sesión, a utilizar en el encabezado Authorization (Bearer) para las siguientes peticiones.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDk0NTY3ODkwIn0.abc123signature", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;


}
