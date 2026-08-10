package com.entysoftware.aplication.model.dto.loginDto;

import java.util.List;


import com.entysoftware.aplication.model.Categorias;
import com.entysoftware.aplication.model.Inventario;
import com.entysoftware.aplication.model.Mesas;

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

    @Schema(description = "Estado actual del establecimiento.", example = "ACTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estado_establecimiento;

    @Schema(description = "Nombre comercial del establecimiento.", example = "Restaurante El Buen Sabor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre_establecimiento;

    @Schema(description = "Listado de mesas registradas en el establecimiento.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Mesas> mesas;

    @Schema(description = "Listado de categorías de productos registradas en el establecimiento.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Categorias> categorias;

    @Schema(description = "Listado de productos del inventario del establecimiento.", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Inventario> inventario;

    @Schema(description = "Token JWT de sesión, a utilizar en el encabezado Authorization (Bearer) para las siguientes peticiones.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDk0NTY3ODkwIn0.abc123signature", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;


}
