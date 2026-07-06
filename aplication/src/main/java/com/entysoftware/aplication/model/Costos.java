package com.entysoftware.aplication.model;

import java.time.LocalDate;

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
@Table(name = "costos")
@Data
@AllArgsConstructor
public class Costos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_costos")
    private Integer idCostos;

    @Column(name = "Fk_id_establecimiento")
    private int idEstablecimiento;

    @Column(name = "valor_costo")
    private int valorCosto;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "fecha_costo")
    private LocalDate fecha;
}
