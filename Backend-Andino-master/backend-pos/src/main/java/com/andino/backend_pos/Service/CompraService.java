package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;

import java.util.List;
import java.util.Optional;


public interface CompraService {

    Compra registrarCompra(Long pedidoId, Compra compra, List<DetalleCompra> detalles);
    List<Compra> findAll();
    Optional<Compra> findById(Long id);
    void deleteById(Long id);
    Compra updateEstado(Long id, String nuevoEstado);
    Compra marcarComoPagada(Long id);

}
