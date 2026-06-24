package com.entysoftware.aplication.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
public class Mesas {

    @Id
    @Column(name = "id_mesa")
    private Integer idMesa; 

    @Column(name = "FK_id_establecimiento")
    private Integer idEstablecimiento;

    @JsonBackReference
    @OneToMany(mappedBy = "idMesa", fetch = FetchType.LAZY)
    private List<EncabezadoPedidos> encabezadoPedido; 
}
