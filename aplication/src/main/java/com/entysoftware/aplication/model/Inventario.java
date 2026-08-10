package com.entysoftware.aplication.model;
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
@NoArgsConstructor
@Entity
@Table(name = "inventario")
@Data
@AllArgsConstructor
@Schema(description = "Producto del inventario de un establecimiento, tal como se devuelve embebido en la respuesta de login.")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    @Schema(description = "Identificador del producto en el inventario.", example = "12", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idInventario;


    @Schema(description = "Nombre del producto.", example = "Hamburguesa clásica")
    private String nombre;

    @Column(name = "FK_categoria")
    @Schema(description = "Identificador de la categoría a la que pertenece el producto.", example = "2")
    private int categoria;

    @Schema(description = "Descripción del producto.", example = "Hamburguesa de carne con queso, lechuga y tomate")
    private String descripcion;

    @Schema(description = "Precio de venta del producto, en la moneda local.", example = "15000")
    private int precio;


}
