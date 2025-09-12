package com.andino.backend_pos.Service;


import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.Venta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VentaService {

    Venta registrarVenta(VentaRequestDTO request);
    FacturaVentaDTO generarFactura(Venta venta);
    List<Venta> findAll();
    Optional<Venta> findById(Long id);
    void deleteById(Long id);

}
