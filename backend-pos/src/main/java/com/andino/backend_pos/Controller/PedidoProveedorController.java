package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.EstadoRequestDTO;
import com.andino.backend_pos.DTOs.PedidoProveedorRequestDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.PedidoProveedorResponseDTO;
import com.andino.backend_pos.Service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoProveedorController {

    private final PedidoService pedidoService;

    public PedidoProveedorController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Crear un nuevo pedido al proveedor
    @PostMapping
    public ResponseEntity<PedidoProveedorResponseDTO> crearPedido(@RequestBody PedidoProveedorRequestDTO request) {
        PedidoProveedorResponseDTO creado = pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Cambiar el estado del pedido (ej. PENDIENTE → ENVIADO → RECIBIDO → PAGADO)
    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoProveedorResponseDTO> cambiarEstado(@PathVariable Long id, @RequestBody EstadoRequestDTO request) {
        PedidoProveedorResponseDTO dto = pedidoService.cambiarEstado(id, request.getEstado());
        return ResponseEntity.ok(dto);
    }

    // Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoProveedorResponseDTO> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoProveedorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
}
