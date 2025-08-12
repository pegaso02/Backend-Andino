package com.andino.backend_pos.DTOs;

import java.time.LocalDate;

public class PagoProveedorRequestDTO {

    private LocalDate fecha;
    private Double monto;
    private Long proveedorId;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }
}
