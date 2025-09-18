package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.ResponseDTOs.PedidoProveedorResponseDTO;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoAPIController {

    private final PedidoService pedidoService;

    public PedidoAPIController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoProveedorResponseDTO> getPedidoDetails(@PathVariable Long id) {
        try {
            PedidoProveedorResponseDTO pedido = pedidoService.obtenerPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
