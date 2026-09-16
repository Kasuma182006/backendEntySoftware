package com.entysoftware.aplication.model.models;

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
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    private String nombre;

    @Column(name = "Fk_id_establecimiento")
    private int idEstablecimiento;

}
