package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();
    Proveedor save(Proveedor proveedor);
    Optional<Proveedor> findById(Long id);
    void deleteById(Long id);
}
