package com.entysoftware.aplication.model.models;


import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Entity
@Table(name = "encabezado_pedidos")
@Data
@AllArgsConstructor
public class EncabezadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "FK_id_mesa", referencedColumnName ="id_mesa" )
    private Mesa idMesa;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "estado_pedido")
    private String estadoPedido;

    @Column(name = "valor_domicilio")
    private int valorDomicilio;

    @Column(name = "precio_total")
    private int precioTotal;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;
    
    private String descripcion;

    // orphanRemoval = true: al reemplazar los detalles en editarPedido, las líneas
    // (y sus adiciones, por cascada) que se quitan de la colección se eliminan de la BD.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CuerpoPedido> detalles;


}
