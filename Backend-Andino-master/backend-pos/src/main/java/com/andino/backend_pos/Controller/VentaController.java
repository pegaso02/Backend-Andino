package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.Venta;
import com.andino.backend_pos.Service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<List<Venta>> findAll() {
        List<Venta> ventas = ventaService.findAll();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> findById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.findById(id);
        return venta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        ventaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
