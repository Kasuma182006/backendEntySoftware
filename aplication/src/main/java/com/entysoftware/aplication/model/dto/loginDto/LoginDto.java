package com.entysoftware.aplication.model.dto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Credenciales de acceso para iniciar sesión como propietario o empleado en un establecimiento específico.")
public class LoginDto {

    @Schema(description = "Número de identificación del propietario o empleado.", example = "1094567890", requiredMode = Schema.RequiredMode.REQUIRED)
    private String identificacion;

    @Schema(description = "Contraseña de acceso del usuario.", example = "Sup3rSecret!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(description = "Identificador del establecimiento en el que se desea iniciar sesión.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id_establecimiento;
}
