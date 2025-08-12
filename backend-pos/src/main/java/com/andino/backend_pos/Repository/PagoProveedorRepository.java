package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.PagoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoProveedorRepository extends JpaRepository<PagoProveedor, Long> {
    public List<PagoProveedor> findAll();
    List<PagoProveedor> findByProveedorId(Long proveedorId);
}
