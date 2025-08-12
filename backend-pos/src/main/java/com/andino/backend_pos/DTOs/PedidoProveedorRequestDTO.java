package com.andino.backend_pos.DTOs;

import java.time.LocalDate;
import java.util.List;

public class PedidoProveedorRequestDTO {
    private LocalDate fecha;
    private Long proveedorId;
    private List<DetallePedidoRequestDTO> detalles;

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

    public List<DetallePedidoRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoRequestDTO> detalles) {
        this.detalles = detalles;
    }
}
