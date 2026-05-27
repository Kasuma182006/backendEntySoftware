package com.entysoftware.aplication.model;

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
@Table(name = "cuerpo_pedidos")
@Data
@AllArgsConstructor
public class CuerpoPedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_cuerpo;
    
    private int FK_id_pedido;
    private int Fk_id_inentario;
}
