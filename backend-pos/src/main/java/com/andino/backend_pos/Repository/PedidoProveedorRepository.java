package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
}
