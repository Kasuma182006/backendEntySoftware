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
import com.entysoftware.aplication.model.dto.InventarioDto;
import com.entysoftware.aplication.model.dto.PaginaDto;
import com.entysoftware.aplication.service.interfaces.InventarioInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión del inventario de productos de un establecimiento.")
public class ProductosController {

    private final InventarioInterface inventarioService;

    public ProductosController(InventarioInterface inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(
        summary = "Listar productos del inventario de un establecimiento (paginado)",
        description = "Devuelve una página de los productos del inventario que pertenecen al establecimiento indicado (a través de la categoría de cada producto), ordenados alfabéticamente por nombre. "
            + "El tamaño de página máximo es 100. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de productos obtenida correctamente (el contenido puede estar vacío)."),
        @ApiResponse(responseCode = "400", description = "Algún parámetro está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al consultar los productos.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @GetMapping("/listar-productos/{idEstablecimiento}")
    public ResponseEntity<PaginaDto<InventarioDto>> listarProductos(
        @Parameter(description = "Identificador del establecimiento cuyos productos se desean listar.", example = "1", required = true)
        @PathVariable("idEstablecimiento") Integer idEstablecimiento,
        @Parameter(description = "Número de página a consultar (inicia en 0).", example = "0")
        @RequestParam(name = "pagina", defaultValue = "0") int pagina,
        @Parameter(description = "Cantidad de elementos por página (máximo 100).", example = "10")
        @RequestParam(name = "tamano", defaultValue = "10") int tamano) {
        return inventarioService.listarProductos(idEstablecimiento, pagina, tamano);
    }

    @Operation(
        summary = "Crear un producto",
        description = "Registra un nuevo producto en el inventario, dentro de la categoría indicada en el cuerpo de la petición. "
            + "La categoría debe existir, y el nombre no puede repetirse dentro de esa misma categoría, sin distinguir mayúsculas ni minúsculas. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "La categoría indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"La categoría con ID 2 no existe\",\"path\":\"/productos/crear-producto\"}"))),
        @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre en la categoría.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"409\",\"error\":\"Conflict\",\"mensaje\":\"El producto 'Hamburguesa clásica' ya existe en la categoría con ID 2\",\"path\":\"/productos/crear-producto\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al crear el producto.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PostMapping("/crear-producto")
    public ResponseEntity<InventarioDto> crearProducto(@RequestBody InventarioDto producto) {
        return inventarioService.crearProducto(producto);
    }

    @Operation(
        summary = "Editar un producto",
        description = "Actualiza parcialmente los datos de un producto existente. Los campos enviados como null se conservan sin cambios; el campo idInventario es obligatorio para identificar el registro. "
            + "Enviar el mismo nombre que ya tiene el producto no genera conflicto. Si se cambia la categoría, esta debe existir y el nombre no puede estar repetido en la categoría de destino. Exclusivo del rol ADMINISTRADOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición es inválido o está mal formado.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class))),
        @ApiResponse(responseCode = "404", description = "El producto o la categoría indicada no existe.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"404\",\"error\":\"No Found\",\"mensaje\":\"El producto con ID 12 no existe\",\"path\":\"/productos/editar-producto\"}"))),
        @ApiResponse(responseCode = "409", description = "Ya existe otro producto con ese nombre en la categoría.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class), examples = @ExampleObject(value = "{\"time\":\"2026-09-13T14:32:05.123\",\"status\":\"409\",\"error\":\"Conflict\",\"mensaje\":\"El producto 'Hamburguesa clásica' ya existe en la categoría con ID 2\",\"path\":\"/productos/editar-producto\"}"))),
        @ApiResponse(responseCode = "500", description = "Error interno inesperado al editar el producto.", content = @Content(schema = @Schema(implementation = ControllerAdviceDto.class)))
    })
    @PatchMapping("/editar-producto")
    public ResponseEntity<String> editarProducto(@RequestBody InventarioDto producto) {
        return inventarioService.editarProducto(producto);
    }
}
