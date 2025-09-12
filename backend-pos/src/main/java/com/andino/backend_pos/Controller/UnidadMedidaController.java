package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.UnidadMedida;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
