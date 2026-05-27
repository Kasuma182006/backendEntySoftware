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
@NoArgsConstructor
@Entity
@Table(name = "encabezado_pedidos")
@Data
@AllArgsConstructor
public class EncabezadoPedidos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pedido;
    
    private int FK_id_mesa;
    private String tipo_pago;
    private String estado_pedido;
    private int valor_domicilio;
    private int valor_total;
    private LocalDate fecha_pedido;
}
