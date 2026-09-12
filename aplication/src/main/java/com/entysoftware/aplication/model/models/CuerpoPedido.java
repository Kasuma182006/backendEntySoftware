package com.entysoftware.aplication.model.models;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cuerpo_pedidos")
public class CuerpoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuerpo")
    private Integer idCuerpo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_id_pedido", referencedColumnName = "id_pedido")
    @JsonBackReference
    private EncabezadoPedido pedido;

    @ManyToOne
    @JoinColumn(name = "FK_id_inventario", referencedColumnName = "id_inventario")
    private Inventario idInventario;

    private Integer cantidad;

    /**
     * Adiciones aplicadas a esta línea de producto (tabla intermedia).
     * mappedBy = "cuerpoPedido"  -> FK en cuerpo_pedidos_adiciones.FK_id_cuerpo_pedido.
     * cascade + orphanRemoval    -> el ciclo de vida de las filas intermedias sigue al de la línea.
     * BatchSize                  -> evita N+1 al cargar las adiciones de varias líneas.
     */
    @OneToMany(mappedBy = "cuerpoPedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @BatchSize(size = 100)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CuerpoPedidosAdiciones> adiciones = new ArrayList<>();

    /** Constructor de conveniencia (sin adiciones) para no romper llamadas existentes. */
    public CuerpoPedido(Integer idCuerpo, EncabezadoPedido pedido, Inventario idInventario, Integer cantidad) {
        this.idCuerpo = idCuerpo;
        this.pedido = pedido;
        this.idInventario = idInventario;
        this.cantidad = cantidad;
        this.adiciones = new ArrayList<>();
    }

    /** Sincroniza ambos extremos de la relación bidireccional. */
    public void agregarAdicion(CuerpoPedidosAdiciones adicion) {
        adicion.setCuerpoPedido(this);
        this.adiciones.add(adicion);
    }

    /** Reemplaza el conjunto de adiciones respetando orphanRemoval. */
    public void reemplazarAdiciones(List<CuerpoPedidosAdiciones> nuevasAdiciones) {
        this.adiciones.clear();
        if (nuevasAdiciones != null) {
            nuevasAdiciones.forEach(this::agregarAdicion);
        }
    }
}
