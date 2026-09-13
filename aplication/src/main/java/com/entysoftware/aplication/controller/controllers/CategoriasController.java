package com.entysoftware.aplication.controller.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entysoftware.aplication.controller.controllerAdviceDto.ControllerAdviceDto;
import com.entysoftware.aplication.model.dto.CategoriasDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.service.interfaces.CategoriasInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "Gestión del catálogo de categorías de productos de un establecimiento.")
public class CategoriasController {

    private final CategoriasInterface categoriasService;

    public CategoriasController(CategoriasInterface categoriasService) {
        this.categoriasService = categoriasService;
    }

    @Operation(
        summary = "Listar categorías de un establecimiento (paginado y filtrado por nombre)",
        description = "Devuelve una página de categorías del establecimiento indicado, ordenadas alfabéticamente por nombre. "
            + "Filtro por nombre: sin el parámetro 'nombre' se listan todas; enviándolo se devuelven las categorías cuyo nombre contenga ese texto en cualquier posición, sin distinguir mayúsculas ni minúsculas (por ejemplo 'a' devuelve todas las que tengan una 'a'). "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de categorías obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "Algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar las categorías.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-categorias/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<CategoriasDto>> listarCategorias(
        @Parameter(description = "Identificador del establecimiento cuyas categorías se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Texto a buscar dentro del nombre de la categoría. Opcional: si se omite o llega vacío, se listan todas.", example = "beb")
        @RequestParam(name = "nombre", required = false) String nombre,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return categoriasService.listarCategorias(idEstablecimiento, nombre, pagina, tamano);
    }

    @Operation(
        summary = "Crear una categoría",
        description = "Registra una nueva categoría en el catálogo del establecimiento indicado en el cuerpo de la petición. El nombre no puede repetirse dentro del mismo establecimiento, sin distinguir mayúsculas ni minúsculas. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría creada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre en el establecimiento.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"409\",\"error\":\"Conflict\",\"mensaje\":\"La categoría 'Bebidas' ya existe en el establecimiento con ID 1\",\"path\":\"/categorias/crear-categoria\"}"))),
        @ApiResponse(responseCode = "404", description = "El establecimiento indicado no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El establecimiento con ID 1 no existe\",\"path\":\"/categorias/crear-categoria\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al crear la categoría.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-categoria")
    public ResponseEntity<CategoriasDto> crearCategoria(@RequestBody CategoriasDto categoria) {
        return categoriasService.crearCategoria(categoria);
    }

    @Operation(
        summary = "Editar una categoría",
        description = "Actualiza parcialmente los datos de una categoría existente. Los campos enviados como null se conservan sin cambios; el campo idCategoria es obligatorio para identificar el registro. El nuevo nombre no puede coincidir con el de otra categoría del mismo establecimiento. El establecimiento no se modifica. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "409", description = "Ya existe otra categoría con ese nombre en el establecimiento.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"409\",\"error\":\"Conflict\",\"mensaje\":\"La categoría 'Bebidas' ya existe en el establecimiento con ID 1\",\"path\":\"/categorias/editar-categoria\"}"))),
        @ApiResponse(responseCode = "404", description = "La categoría indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La categoría con ID 2 no existe\",\"path\":\"/categorias/editar-categoria\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar la categoría.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-categoria")
    public ResponseEntity<String> editarCategoria(@RequestBody CategoriasDto categoria) {
        return categoriasService.editarCategoria(categoria);
    }
}
