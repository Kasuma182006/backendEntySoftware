package com.entysoftware.aplication.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.service.InventarioInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Consulta del inventario de productos.")
public class ProductosController {

    private final InventarioInterface inventarioService;

    public ProductosController(InventarioInterface inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(
        summary = "Listar productos del inventario de un establecimiento",
        description = "Devuelve todos los productos del inventario que pertenecen al establecimiento indicado (a través de la categoría de cada producto), con la totalidad de sus campos, en la capa de transporte para el cliente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente (puede estar vacío)."),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los productos.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-productos/{idEstablecimiento}")
    public ResponseEntity<List<InventarioDto>> listarProductos(
        @Parameter(description = "Identificador del establecimiento cuyos productos se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento) {
        return inventarioService.listarProductos(idEstablecimiento);
    }
}
