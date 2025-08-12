package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaNombre(String nombreCategoria);
    Producto findByNombreContainingIgnoreCase(String nombre);
}
