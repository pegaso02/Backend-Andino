package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;

import java.util.List;


public interface CompraService {

    public Compra registrarCompra(Long pedidoId, Compra compra, List<DetalleCompra> detalles);

}
