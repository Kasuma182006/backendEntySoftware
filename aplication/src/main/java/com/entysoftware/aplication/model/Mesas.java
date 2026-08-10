package com.entysoftware.aplication.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
@Schema(description = "Mesa de un establecimiento, tal como se devuelve embebida en la respuesta de login.")
public class Mesas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    @Schema(description = "Identificador de la mesa.", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idMesa;

    @Column(name = "FK_id_establecimiento")
    @Schema(description = "Identificador del establecimiento al que pertenece la mesa.", example = "1")
    private Integer idEstablecimiento;

    @Column(name = "nombre")
    @Schema(description = "Nombre o número identificador de la mesa.", example = "Mesa 5")
    private String nombreMesa;

    @Column (name = "ocupada")
    @Schema(description = "Indica si la mesa está ocupada (true) o libre (false).", example = "false")
    private Boolean estadoMesa;

    @JsonBackReference
    @OneToMany(mappedBy = "idMesa", fetch = FetchType.LAZY)
    @Schema(hidden = true)
    private List<EncabezadoPedidos> encabezadoPedido;
}
