package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/vendedor/categorias")
public class VendedorCategoriaController {

    private final CategoriaService categoriaService;

    public VendedorCategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listCategorias(Model model) {
        List<com.andino.backend_pos.Model.Categoria> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "vendedor/categorias/list";
    }
}
