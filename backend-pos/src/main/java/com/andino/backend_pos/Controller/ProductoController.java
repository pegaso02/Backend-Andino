package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")

public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto guardado = productoService.guardarProducto(producto);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }



}
