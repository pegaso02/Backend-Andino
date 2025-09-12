package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.DetalleVenta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DetalleVentaService {
    List<DetalleVenta> findAll();
    List<DetalleVenta> findAllOrderByVentaIdAsc();
}
