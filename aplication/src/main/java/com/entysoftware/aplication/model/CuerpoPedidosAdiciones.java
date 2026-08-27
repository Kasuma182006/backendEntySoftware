package com.entysoftware.aplication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Tabla intermedia entre "cuerpo_pedidos" y "adiciones".
 * Cada fila = una adición concreta aplicada a una línea de producto de un pedido.
 *
 * Correspondencia propiedad Java -> columna BD:
 *   idPedidoAdicion         -> cuerpo_pedidos_adiciones.id_pedido_adicion   (PK, autoincremental)
 *   cuerpoPedido (idCuerpo) -> cuerpo_pedidos_adiciones.FK_id_cuerpo_pedido (FK a cuerpo_pedidos.id_cuerpo)
 *   adicion (idAdicion)     -> cuerpo_pedidos_adiciones.FK_id_adicion       (FK a adiciones.id_adicion)
 *   cantidadAdicion         -> cuerpo_pedidos_adiciones.cantidad_adicion
 *
 * NOTA (asunción): el nombre de la tabla no fue proporcionado; se asume
 * "cuerpo_pedidos_adiciones" por consistencia con "cuerpo_pedidos".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cuerpo_pedidos_adiciones")
public class CuerpoPedidosAdiciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_adicion")
    private Integer idPedidoAdicion;

    /** Lado propietario de la relación con la línea de pedido. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_id_cuerpo_pedido", referencedColumnName = "id_cuerpo")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CuerpoPedidos cuerpoPedido;

    /** Adición del catálogo. Solo lectura desde aquí: no se cascadea. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_id_adicion", referencedColumnName = "id_adicion")
    private Adiciones adicion;

    @Column(name = "cantidad_adicion")
    private Integer cantidadAdicion;

    /** Constructor de conveniencia usado por la capa de servicio al construir el grafo. */
    public CuerpoPedidosAdiciones(CuerpoPedidos cuerpoPedido, Adiciones adicion, Integer cantidadAdicion) {
        this.cuerpoPedido = cuerpoPedido;
        this.adicion = adicion;
        this.cantidadAdicion = cantidadAdicion;
    }
}
