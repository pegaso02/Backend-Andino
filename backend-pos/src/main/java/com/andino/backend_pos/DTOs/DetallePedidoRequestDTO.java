package com.andino.backend_pos.DTOs;

import com.andino.backend_pos.Model.UnidadMedida;

import java.math.BigDecimal;

public class DetallePedidoRequestDTO {

    private Long productoId;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;

    private Long UnidadMedidaId;

    public Long getUnidadMedidaId() {
        return UnidadMedidaId;
    }

    public void setUnidadMedidaId(Long unidadMedidaId) {
        UnidadMedidaId = unidadMedidaId;
    }
//    private UnidadMedida unidadMedida;
//
//
//    public UnidadMedida getUnidadMedida() {
//        return unidadMedida;
//    }
//
//    public void setUnidadMedida(UnidadMedida unidadMedida) {
//        this.unidadMedida = unidadMedida;
//    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
