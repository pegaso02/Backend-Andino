package com.andino.backend_pos.DTOs.ResponseDTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FacturaVentaDTO {

    private Long ventaId;
    private LocalDate fecha;
    private List<LineaFacturaDTO> detalles;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<LineaFacturaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<LineaFacturaDTO> detalles) {
        this.detalles = detalles;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
