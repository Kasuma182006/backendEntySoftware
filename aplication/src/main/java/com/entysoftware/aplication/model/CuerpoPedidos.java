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
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cuerpo_pedidos")
public class CuerpoPedidos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuerpo")
    private Integer idCuerpo;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_id_pedido", referencedColumnName = "id_pedido") 
    @JsonBackReference
    private EncabezadoPedidos pedido; 
    
    @ManyToOne
    @JoinColumn(name = "FK_id_inventario", referencedColumnName = "id_inventario") 
    private Inventario idInventario;

    private Integer cantidad;
}
