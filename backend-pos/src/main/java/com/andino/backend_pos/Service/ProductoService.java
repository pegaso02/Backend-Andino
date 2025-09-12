package com.andino.backend_pos.Service;


import com.andino.backend_pos.Model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoService {

    Producto guardarProducto(Producto producto);
    List<Producto> listarProductos();
    Optional<Producto> obtenerPorId(Long id);
    Producto actualizarProducto(Long id, Producto productoActualizado);
    void eliminarProducto(Long id);

}
