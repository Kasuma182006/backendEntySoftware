package com.entysoftware.aplication.model;

import org.hibernate.annotations.BatchSize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Catálogo de adiciones (tabla principal "adiciones").
 *
 * Correspondencia propiedad Java -> columna BD:
 *   idAdicion         -> adiciones.id_adicion            (PK, autoincremental)
 *   idEstablecimiento -> adiciones.FK_id_establecimiento (FK a establecimiento.id_establecimiento)
 *   nombre            -> adiciones.nombre
 *
 * NOTA (asunción): el nombre real de la columna de establecimiento no fue
 * proporcionado. Se asume "FK_id_establecimiento" siguiendo la convención del
 * resto del modelo (ver {@link Inventario#getCategoria()} -> "FK_categoria").
 * Ajustar el value de @Column si el DDL usa otro nombre.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "adiciones")
@BatchSize(size = 100) // mitiga N+1 al resolver la relación @ManyToOne desde CuerpoPedidosAdiciones
@Schema(description = "Adición del catálogo del establecimiento que puede agregarse a un producto de un pedido.")
public class Adiciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adicion")
    @Schema(description = "Identificador de la adición.", example = "7", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idAdicion;

    @Column(name = "FK_id_establecimiento") // TODO(confirmar nombre de columna con el DDL)
    @Schema(description = "Identificador del establecimiento dueño de la adición.", example = "1")
    private Integer idEstablecimiento;

    @Schema(description = "Nombre de la adición.", example = "Queso extra")
    private String nombre;

    @Column(name = "precio")
    private Integer precioAdicion;
}
