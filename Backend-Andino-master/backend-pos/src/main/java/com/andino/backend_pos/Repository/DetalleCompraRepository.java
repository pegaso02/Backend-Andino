package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {

    List<DetalleCompra> findByCompra(Compra compra);

}
