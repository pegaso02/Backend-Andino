package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
    @Query("SELECT p FROM PedidoProveedor p LEFT JOIN FETCH p.proveedor LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto LEFT JOIN FETCH d.unidadMedida")
    List<PedidoProveedor> findAllWithDetails();

    List<PedidoProveedor> findByEstado(String estado);

    @Query("SELECT p FROM PedidoProveedor p LEFT JOIN FETCH p.proveedor LEFT JOIN FETCH p.detalles d LEFT JOIN FETCH d.producto LEFT JOIN FETCH d.unidadMedida WHERE p.id = :id")
    Optional<PedidoProveedor> findByIdWithDetails(Long id);
}
