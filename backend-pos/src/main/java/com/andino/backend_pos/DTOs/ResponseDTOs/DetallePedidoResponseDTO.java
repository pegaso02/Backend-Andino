package com.andino.backend_pos.DTOs.ResponseDTOs;

import java.math.BigDecimal;

public class DetallePedidoResponseDTO {

    private Long productoId;

    private BigDecimal cantidad;
    private String unidadSimbolo;
    private BigDecimal precioUnitario;

    public String getCantidadFormateada() {
        return cantidad.stripTrailingZeros().toPlainString() + " " + unidadSimbolo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadSimbolo() {
        return unidadSimbolo;
    }

    public void setUnidadSimbolo(String unidadSimbolo) {
        this.unidadSimbolo = unidadSimbolo;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }


}
