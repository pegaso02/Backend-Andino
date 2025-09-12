package com.andino.backend_pos.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompraRequestDTO {

    private LocalDate fecha;
    private Long proveedorId;
    private List<DetalleCompraRequest> detalles;
    private Long pedidoProveedorId;

    public Long getPedidoProveedorId() {
        return pedidoProveedorId;
    }

    public void setPedidoProveedorId(Long pedidoProveedorId) {
        this.pedidoProveedorId = pedidoProveedorId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public List<DetalleCompraRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraRequest> detalles) {
        this.detalles = detalles;
    }
}
