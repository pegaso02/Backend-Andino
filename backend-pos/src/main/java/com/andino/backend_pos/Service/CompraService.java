package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;

import java.util.List;
import java.util.Optional;


public interface CompraService {

    public Compra registrarCompra(Long pedidoId, Compra compra, List<DetalleCompra> detalles);

    Optional<Compra> findById(Long id);

    List<Compra> findAll();

    void updateEstado(Long id, String estado);

    void marcarComoPagada(Long id);

    void deleteById(Long id);
}
