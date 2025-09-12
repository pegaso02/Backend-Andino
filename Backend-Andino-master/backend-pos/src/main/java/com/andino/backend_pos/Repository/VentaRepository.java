package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.usuario")
    List<Venta> findAllWithDetails();
}

