package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.UnidadMedida;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unidadMedida")
public class UnidadMedidaController {

    public final UnidadMedidaRepository unidadMedidaRepository;

    public UnidadMedidaController(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @PostMapping
    public ResponseEntity<UnidadMedida> save(@RequestBody UnidadMedida unidadMedida) {
        UnidadMedida UMedida = unidadMedidaRepository.save(unidadMedida);
        return ResponseEntity.ok(UMedida);
    }

    @GetMapping
    public ResponseEntity<List<UnidadMedida>> findAll() {
        List<UnidadMedida> unidades = unidadMedidaRepository.findAll();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedida> findById(@PathVariable Long id) {
        Optional<UnidadMedida> unidadMedida = unidadMedidaRepository.findById(id);
        return unidadMedida.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (unidadMedidaRepository.existsById(id)) {
            unidadMedidaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
