package com.andino.backend_pos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private Double total;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public PedidoProveedor getPedidoProveedor() {
        return pedidoProveedor;
    }

    public void setPedidoProveedor(PedidoProveedor pedidoProveedor) {
        this.pedidoProveedor = pedidoProveedor;
    }

    @ManyToOne
    private PedidoProveedor pedidoProveedor;

    @ManyToOne
    private Proveedor proveedor;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}

