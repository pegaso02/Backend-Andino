package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Proveedor;
import com.andino.backend_pos.Repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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



}
