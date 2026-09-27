package com.entysoftware.aplication.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.EmpleadoDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.service.interfaces.EmpleadosInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empleados")
@Tag(name = "Empleados", description = "Consulta de los empleados de un establecimiento.")
public class EmpleadosController {

    private final EmpleadosInterface empleadosService;

    public EmpleadosController(EmpleadosInterface empleadosService) {
        this.empleadosService = empleadosService;
    }

    @Operation(
        summary = "Listar empleados de un establecimiento (paginado)",
        description = "Devuelve una página de empleados del establecimiento indicado, ordenados alfabéticamente por nombre. "
            + "No incluye el identificador ni la contraseña del empleado. "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de empleados obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "Algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los empleados.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-empleados/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<EmpleadoDto>> listarEmpleados(
        @Parameter(description = "Identificador del establecimiento cuyos empleados se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return empleadosService.listarEmpleados(idEstablecimiento, pagina, tamano);
    }
}
