package com.entysoftware.aplication.controller.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.AdicionesDto;
import com.entysoftware.aplication.service.interfaces.AdicionesInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/adiciones")
@Tag(name = "Adiciones", description = "Gestión CRUD del catálogo de adiciones de un establecimiento.")
public class AdicionesController {

    private final AdicionesInterface adicionesService;

    public AdicionesController(AdicionesInterface adicionesService) {
        this.adicionesService = adicionesService;
    }

    @Operation(
        summary = "Listar adiciones de un establecimiento",
        description = "Devuelve todas las adiciones del catálogo que pertenecen al establecimiento indicado. Disponible para los roles ADMINISTRADOR y ASISTENTE."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de adiciones obtenido correctamente (puede estar vacío)."),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar las adiciones.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-adiciones/{idEstablecimiento}")
    public ResponseEntity<List<AdicionesDto>> listarAdiciones(
        @Parameter(description = "Identificador del establecimiento cuyas adiciones se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return adicionesService.listarAdiciones(idEstablecimiento);
    }

    @Operation(
        summary = "Crear una adición",
        description = "Registra una nueva adición en el catálogo del establecimiento indicado en el cuerpo de la petición. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Adición creada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/adiciones/crear-adicion\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al crear la adición.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-adicion")
    public ResponseEntity<AdicionesDto> crearAdicion(@RequestBody AdicionesDto adicion) {
        return adicionesService.crearAdicion(adicion);
    }

    @Operation(
        summary = "Editar una adición",
        description = "Actualiza parcialmente los datos de una adición existente. Los campos enviados como null se conservan sin cambios; el campo idAdicion es obligatorio para identificar el registro a actualizar. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Adición actualizada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "La adición indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La adición con ID 7 no existe\",\"path\":\"/adiciones/editar-adicion\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar la adición.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-adicion")
    public ResponseEntity<String> editarAdicion(@RequestBody AdicionesDto adicion) {
        return adicionesService.editarAdicion(adicion);
    }

   /* @Operation(
        summary = "Eliminar una adición",
        description = "Elimina de forma permanente la adición indicada por su identificador. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Adición eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "La adición indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-08-10T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La adición con ID 7 no existe\",\"path\":\"/adiciones/eliminar-adicion/7\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al eliminar la adición.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @DeleteMapping("/eliminar-adicion/{idAdicion}")
    public ResponseEntity<String> eliminarAdicion(
        @Parameter(description = "Identificador de la adición a eliminar.", example = "7", required = true)
        @PathVariable("idAdicion") Integer idAdicion) {
        return adicionesService.eliminarAdicion(idAdicion);
    }*/
}
