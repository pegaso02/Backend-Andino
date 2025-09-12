package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Categoria;
import com.andino.backend_pos.Repository.CategoriaRepository;
import com.andino.backend_pos.Service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;

    public AdminCategoriaController(CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listCategorias(Model model) {
        List<Categoria> categorias = categoriaService.findAll();
        model.addAttribute("categorias", categorias);
        return "admin/categorias/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/categorias/form";
    }

    @PostMapping("/save")
    public String saveCategoria(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttributes) {
        categoriaRepository.save(categoria);
        redirectAttributes.addFlashAttribute("message", "Categoría guardada exitosamente!");
        return "redirect:/admin/categorias";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent()) {
            model.addAttribute("categoria", categoria.get());
            return "admin/categorias/form";
        } else {
            return "redirect:/admin/categorias";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoriaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Categoría eliminada exitosamente!");
        return "redirect:/admin/categorias";
    }
}
