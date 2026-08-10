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
@Table(name = "categorias")
@Data
@AllArgsConstructor
@Schema(description = "Categoría de productos del inventario de un establecimiento, tal como se devuelve embebida en la respuesta de login.")
public class Categorias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la categoría.", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id ;

    @Schema(description = "Nombre de la categoría.", example = "Bebidas")
    private String nombre;

    @Column(name = "Fk_id_establecimiento")
    @Schema(description = "Identificador del establecimiento al que pertenece la categoría.", example = "1")
    private int idEstablecimiento;

}
