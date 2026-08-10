package com.entysoftware.aplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.MesasDto;
import com.entysoftware.aplication.service.MesasInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/mesas")
@Tag(name = "Mesas", description = "Gestión CRUD de las mesas de un establecimiento.")
public class MesasControler {
    private final MesasInterface mesasService;
    public MesasControler(MesasInterface mesasService){
        this.mesasService = mesasService;
    }

    @Operation(
        summary = "Listar mesas de un establecimiento",
        description = "Devuelve todas las mesas registradas para el establecimiento indicado, incluyendo su estado (ocupada/libre)."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de mesas obtenido correctamente (puede estar vacío)."),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar las mesas.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-mesas/{idEstablecimiento}")
    public ResponseEntity<List<MesasDto>> listarMesas(
        @Parameter(description = "Identificador del establecimiento cuyas mesas se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return mesasService.listarMesas(idEstablecimiento);
    }

    @Operation(
        summary = "Crear una mesa",
        description = "Registra una nueva mesa para el establecimiento indicado en el cuerpo de la petición."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesa creada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/mesas/crear-mesa\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al crear la mesa.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-mesa")
    public ResponseEntity<MesasDto> crearMesa(@RequestBody MesasDto mesa) {
        return mesasService.crearMesa(mesa);
    }

    @Operation(
        summary = "Editar una mesa",
        description = "Actualiza parcialmente los datos de una mesa existente. Los campos enviados como null se conservan sin cambios; el campo idMesa es obligatorio para identificar el registro a actualizar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesa actualizada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "La mesa indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La mesa con ID 3 no existe\",\"path\":\"/mesas/editar-mesa\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar la mesa.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-mesa")
    public ResponseEntity<String> editarMesa(@RequestBody MesasDto mesa) {
        return mesasService.editarMesa(mesa);
    }

    @Operation(
        summary = "Eliminar una mesa",
        description = "Elimina de forma permanente la mesa indicada por su identificador."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesa eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "La mesa indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La mesa con ID 3 no existe\",\"path\":\"/mesas/eliminar-mesa/3\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al eliminar la mesa.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @DeleteMapping("/eliminar-mesa/{idMesa}")
    public ResponseEntity<String> eliminarMesa(
        @Parameter(description = "Identificador de la mesa a eliminar.", example = "3", required = true)
        @PathVariable("idMesa") Integer idMesa) {
        return mesasService.eliminarMesa(idMesa);
    }

}
