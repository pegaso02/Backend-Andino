package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.CompraRequestDTO;
import com.andino.backend_pos.Model.*;
import com.andino.backend_pos.Repository.PedidoProveedorRepository;
import com.andino.backend_pos.Repository.ProductoRepository;
import com.andino.backend_pos.Repository.ProveedorRepository;
import com.andino.backend_pos.Service.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/compras")

public class CompraController {

    private final CompraService compraService;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final PedidoProveedorRepository pedidoProveedorRepository;

    public CompraController(CompraService compraService, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, PedidoProveedorRepository pedidoProveedorRepository    ){
        this.compraService = compraService;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.pedidoProveedorRepository = pedidoProveedorRepository;
    }

    @PostMapping
    public ResponseEntity<?> registrarCompra(@RequestBody CompraRequestDTO request) {
        try {
            // ✅ Validación: pedidoProveedorId no debe ser null
            if (request.getPedidoProveedorId() == null) {
                return ResponseEntity.badRequest().body("El campo 'pedidoProveedorId' es obligatorio");
            }

            // ✅ Log temporal (puedes usar logger también)
            System.out.println("pedidoProveedorId = " + request.getPedidoProveedorId());

            // ✅ Validar que el pedido exista antes de continuar
            if (!pedidoProveedorRepository.existsById(request.getPedidoProveedorId())) {
                return ResponseEntity.badRequest().body("El pedido con ID " + request.getPedidoProveedorId() + " no existe");
            }

            Compra compra = new Compra();
            compra.setFecha(request.getFecha());
            compra.setTotal(0.0); // Se recalcula en el servicio

            List<DetalleCompra> detalles = request.getDetalles().stream().map(det -> {
                System.out.println(request.getDetalles()+"DETALLES EN CONTROLLER");
                DetalleCompra d = new DetalleCompra();
                d.setCantidad(det.getCantidad());
                d.setPrecioUnitario(det.getPrecioUnitario());
                d.setProducto(productoRepository.findById(det.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + det.getProductoId())));
                return d;
            }).collect(Collectors.toList());

            Compra nueva = compraService.registrarCompra(
                    request.getPedidoProveedorId(),
                    compra,
                    detalles
            );

            return ResponseEntity.ok(nueva);

        } catch (RuntimeException e) {
            // ✅ Captura errores y evita 500 sin mensaje claro
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Compra>> findAll() {
        List<Compra> compras = compraService.findAll();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> findById(@PathVariable Long id) {
        Optional<Compra> compra = compraService.findById(id);
        return compra.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Compra> updateEstado(@PathVariable Long id, @RequestBody String nuevoEstado) {
        Compra compra = compraService.updateEstado(id, nuevoEstado);
        return ResponseEntity.ok(compra);
    }

    @PutMapping("/{id}/pagada")
    public ResponseEntity<Compra> marcarComoPagada(@PathVariable Long id) {
        Compra compra = compraService.marcarComoPagada(id);
        return ResponseEntity.ok(compra);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        compraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
