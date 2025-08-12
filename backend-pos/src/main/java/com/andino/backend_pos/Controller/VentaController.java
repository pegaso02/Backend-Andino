package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.Venta;
import com.andino.backend_pos.Service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<FacturaVentaDTO> registrarVenta(@RequestBody VentaRequestDTO request) {
        Venta venta = ventaService.registrarVenta(request);
        FacturaVentaDTO factura = ventaService.generarFactura(venta);
        return ResponseEntity.ok(factura);
    }

}
