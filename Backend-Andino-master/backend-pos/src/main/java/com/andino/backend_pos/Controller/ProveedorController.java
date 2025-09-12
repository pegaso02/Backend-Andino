package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Proveedor;
import com.andino.backend_pos.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorRepository proveedorRepository;

    public ProveedorController(ProveedorRepository proveedorRepository){
        this.proveedorRepository = proveedorRepository;
    }

    @PostMapping
    public ResponseEntity<Proveedor> save(@RequestBody Proveedor proveedor) {
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return ResponseEntity.ok().body(proveedorGuardado);
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> findAll() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return ResponseEntity.ok().body(proveedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> findById(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        return proveedor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
