package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Proveedor;
import com.andino.backend_pos.Service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/proveedores")
public class AdminProveedorController {

    private final ProveedorService proveedorService;

    public AdminProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listProveedores(Model model) {
        List<Proveedor> proveedores = proveedorService.findAll();
        model.addAttribute("proveedores", proveedores);
        return "admin/proveedores/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "admin/proveedores/form";
    }

    @PostMapping("/save")
    public String saveProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) {
        proveedorService.save(proveedor);
        redirectAttributes.addFlashAttribute("message", "Proveedor guardado exitosamente!");
        return "redirect:/admin/proveedores";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Proveedor> proveedor = proveedorService.findById(id);
        if (proveedor.isPresent()) {
            model.addAttribute("proveedor", proveedor.get());
            return "admin/proveedores/form";
        } else {
            return "redirect:/admin/proveedores";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProveedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        proveedorService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Proveedor eliminado exitosamente!");
        return "redirect:/admin/proveedores";
    }
}
