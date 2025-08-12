package com.andino.backend_pos.Service;


import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.Venta;

public interface VentaService {

    public Venta registrarVenta(VentaRequestDTO request);
    public FacturaVentaDTO generarFactura(Venta venta);

}
