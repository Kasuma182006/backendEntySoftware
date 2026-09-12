package com.entysoftware.aplication.model.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta paginada genérica. Contiene los elementos de la página solicitada y los metadatos de paginación.")
public class PaginaDto<T> {

    @Schema(description = "Elementos de la página actual.")
    private List<T> contenido;

    @Schema(description = "Número de la página actual (inicia en 0).", example = "0")
    private int paginaActual;

    @Schema(description = "Cantidad máxima de elementos por página.", example = "10")
    private int tamanoPagina;

    @Schema(description = "Cantidad total de elementos que cumplen el filtro.", example = "37")
    private long totalElementos;

    @Schema(description = "Cantidad total de páginas disponibles.", example = "4")
    private int totalPaginas;

    @Schema(description = "Indica si la página actual es la primera.", example = "true")
    private boolean primera;

    @Schema(description = "Indica si la página actual es la última.", example = "false")
    private boolean ultima;

    public static <T> PaginaDto<T> desdePage(Page<T> pagina) {
        return new PaginaDto<>(
            pagina.getContent(),
            pagina.getNumber(),
            pagina.getSize(),
            pagina.getTotalElements(),
            pagina.getTotalPages(),
            pagina.isFirst(),
            pagina.isLast()
        );
    }
}
