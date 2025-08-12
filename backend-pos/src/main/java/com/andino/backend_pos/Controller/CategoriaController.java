package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Categoria;
import com.andino.backend_pos.Repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    public final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @PostMapping
    public ResponseEntity<Categoria> CategoriaController(@RequestBody Categoria categoria) {
        Categoria categoria1 = categoriaRepository.save(categoria);
        return ResponseEntity.ok(categoria1);
    }
}
