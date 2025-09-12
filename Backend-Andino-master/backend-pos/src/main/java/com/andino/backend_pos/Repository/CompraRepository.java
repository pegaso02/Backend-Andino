package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c FROM Compra c LEFT JOIN FETCH c.pedidoProveedor LEFT JOIN FETCH c.proveedor")
    List<Compra> findAllWithDetails();
}
