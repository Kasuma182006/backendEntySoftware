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
@Table(name = "costos")
@Data
@AllArgsConstructor
public class Costos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_costos; 

    private int Fk_id_establecimiento;
    private int valor_costo;
    
}
