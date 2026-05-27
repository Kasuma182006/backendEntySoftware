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
@Table(name = "mesas")
@Data
@AllArgsConstructor
public class Mesas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_mesa;

    private String nombre;
    private boolean ocupada;
    private int FK_id_establecimiento;
}
