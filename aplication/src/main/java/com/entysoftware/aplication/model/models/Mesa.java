package com.entysoftware.aplication.model.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
@Schema(description = "Mesa de un establecimiento, tal como se devuelve embebida en la respuesta de login.")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Integer idMesa;

    @Column(name = "FK_id_establecimiento")
    private Integer idEstablecimiento;

    @Column(name = "nombre")
    private String nombreMesa;

    @Column (name = "ocupada")
    private Boolean estadoMesa;

    @JsonBackReference
    @OneToMany(mappedBy = "idMesa", fetch = FetchType.LAZY)
    @Schema(hidden = true)
    private List<EncabezadoPedido> encabezadoPedido;

   
    private Boolean eliminada; 
}
