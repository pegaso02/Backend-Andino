package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.DetalleVenta;
import com.andino.backend_pos.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.producto JOIN FETCH d.venta")
    List<DetalleVenta> findAllWithDetails();

    List<DetalleVenta> findAllByOrderByVentaIdAsc();

}
