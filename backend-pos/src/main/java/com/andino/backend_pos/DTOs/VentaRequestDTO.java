package com.andino.backend_pos.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VentaRequestDTO {
    private LocalDate fecha;
    private Long usuarioId;
    private BigDecimal iva;
    private List<DetalleVentaRequestDTO> detalles;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public List<DetalleVentaRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaRequestDTO> detalles) {
        this.detalles = detalles;
    }
}
