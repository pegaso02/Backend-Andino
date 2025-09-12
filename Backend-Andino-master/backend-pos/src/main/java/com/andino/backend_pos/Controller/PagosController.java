package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.ResponseDTOs.PagoProveedorResponseDTO;
import com.andino.backend_pos.Service.PagoProveedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pago")

public class PagosController {

    private final PagoProveedorService pagoProveedorService;

    public PagosController(PagoProveedorService pagoProveedorService) {
        this.pagoProveedorService = pagoProveedorService;
    }

    @GetMapping("/pagos/proveedor/{id}")
    public ResponseEntity<List<PagoProveedorResponseDTO>> listarPagosProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(pagoProveedorService.obtenerPagosPorProveedor(id));
    }
}
