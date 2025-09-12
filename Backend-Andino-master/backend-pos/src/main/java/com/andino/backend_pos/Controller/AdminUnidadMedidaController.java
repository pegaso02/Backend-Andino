package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.UnidadMedida;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import com.andino.backend_pos.Service.UnidadMedidaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/unidades")
public class AdminUnidadMedidaController {

    private final UnidadMedidaService unidadMedidaService;
    private final UnidadMedidaRepository unidadMedidaRepository;

    public AdminUnidadMedidaController(UnidadMedidaService unidadMedidaService, UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaService = unidadMedidaService;
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @GetMapping
    public String listUnidades(Model model) {
        List<UnidadMedida> unidades = unidadMedidaService.findAll();
        model.addAttribute("unidades", unidades);
        return "admin/unidades/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("unidadMedida", new UnidadMedida());
        return "admin/unidades/form";
    }

    @PostMapping("/save")
    public String saveUnidadMedida(@ModelAttribute UnidadMedida unidadMedida, RedirectAttributes redirectAttributes) {
        unidadMedidaRepository.save(unidadMedida);
        redirectAttributes.addFlashAttribute("message", "Unidad de medida guardada exitosamente!");
        return "redirect:/admin/unidades";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<UnidadMedida> unidadMedida = unidadMedidaRepository.findById(id);
        if (unidadMedida.isPresent()) {
            model.addAttribute("unidadMedida", unidadMedida.get());
            return "admin/unidades/form";
        } else {
            return "redirect:/admin/unidades";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUnidadMedida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        unidadMedidaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Unidad de medida eliminada exitosamente!");
        return "redirect:/admin/unidades";
    }
}
