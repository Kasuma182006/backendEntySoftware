package com.entysoftware.aplication.model.dto.pedidosDTOs;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Línea de detalle de un pedido: producto del inventario, cantidad y adiciones solicitadas.")
public class DetallesPedidoDto
{
    @Schema(description = "Identificador de la línea de detalle en el pedido. Generado por el sistema; se ignora al crear un pedido.", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    Integer idCuerpoPedido;

    @Schema(description = "Identificador del producto del inventario solicitado.", example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer idProducto;

    @Schema(description = "Nombre del producto solicitado. Se completa automáticamente a partir del inventario; se ignora al crear un pedido.", example = "Hamburguesa clásica", accessMode = Schema.AccessMode.READ_ONLY)
    String nombre;

    @Schema(description = "Cantidad de unidades solicitadas del producto.", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer cantidad;

    @Schema(description = "Adiciones aplicadas a esta línea de producto.")
    List<AdicionPedidoDto> adiciones = new ArrayList<>();

    /** Constructor de conveniencia sin adiciones (compatibilidad con llamadas previas). */
    public DetallesPedidoDto(Integer idCuerpoPedido, Integer idProducto, String nombre, Integer cantidad) {
        this.idCuerpoPedido = idCuerpoPedido;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.adiciones = new ArrayList<>();
    }

    public List<AdicionPedidoDto> getAdiciones() {
        return adiciones != null ? adiciones : new ArrayList<>();
    }
 }
