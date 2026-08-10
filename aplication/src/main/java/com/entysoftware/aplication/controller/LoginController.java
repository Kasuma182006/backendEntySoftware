package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.loginDto.EstablecimientosDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginDto;
import com.entysoftware.aplication.model.dto.loginDto.LoginSuccesfulDto;
import com.entysoftware.aplication.service.LoginInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/")
@Slf4j
@Tag(name = "Autenticación", description = "Búsqueda de establecimientos por identificación e inicio de sesión de propietarios y empleados.")
public class LoginController {

    private final LoginInterface loginInterface;

    public LoginController(LoginInterface loginInterface){
        this.loginInterface = loginInterface;
    }

    @Operation(
        summary = "Buscar establecimientos por identificación",
        description = "Devuelve la lista de establecimientos asociados a un número de identificación, ya sea como propietario o como empleado. Se utiliza antes del login para que el usuario seleccione el establecimiento en el que desea iniciar sesión."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de establecimientos encontrados (puede estar vacío si el identificador no tiene ninguno asociado y no se le considera un error)."),
        @ApiResponse(responseCode = "404", description = "No se encontraron establecimientos asociados a la identificación proporcionada.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(name = "Usuario no encontrado", value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"Not Found\",\"mensaje\":\"No se han encontrado coincidencias\",\"path\":\"/buscar-establecimiento/1094567890\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al buscar los establecimientos.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/buscar-establecimiento/{idPersona}")
    public ResponseEntity<List<EstablecimientosDto>> buscarEstablecimientosPorIdentificacion(
        @Parameter(description = "Número de identificación del propietario o empleado.", example = "1094567890", required = true)
        @PathVariable("idPersona") String idPersona) {

        log.debug("buscando establecimientos del siguiente id ... {} ", idPersona);

        return loginInterface.ubicarEstablecimiento(idPersona);
    }


    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica a un propietario o empleado dentro de un establecimiento específico usando identificación y contraseña, y devuelve un token JWT junto con el estado inicial del establecimiento (mesas, categorías e inventario)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso. Se retorna el token JWT y los datos iniciales del establecimiento."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido, está mal formado o el identificador del establecimiento no es numérico.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe, o las credenciales (identificación/contraseña) no coinciden con ningún propietario o empleado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = {
            @ExampleObject(name = "Establecimiento no encontrado", value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"No se ha encontrado el ID del establecimiento\",\"path\":\"/login\"}"),
            @ExampleObject(name = "Credenciales inválidas", value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"Not Found\",\"mensaje\":\"No se han encontrado resultados\",\"path\":\"/login\"}")
        })),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado durante el proceso de login.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<LoginSuccesfulDto> login(@RequestBody LoginDto usuario) {

        log.debug("login entrante: {} ", usuario);

        return loginInterface.login(usuario);
    }


}
