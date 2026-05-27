package com.entysoftware.aplication.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "base_inicial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseInicial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_base; 

    private int Fk_id_establecimiento;
    private int valor; 
    private LocalDate fecha;
}
