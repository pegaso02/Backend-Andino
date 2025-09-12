package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Service.CategoriaService;
import com.andino.backend_pos.Service.ProductoService;
import com.andino.backend_pos.Service.ProveedorService;
import com.andino.backend_pos.Service.UnidadMedidaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final UnidadMedidaService unidadMedidaService;

    public AdminProductoController(ProductoService productoService, CategoriaService categoriaService, ProveedorService proveedorService, UnidadMedidaService unidadMedidaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
        this.unidadMedidaService = unidadMedidaService;
    }

    @GetMapping
    public String listProductos(Model model) {
        List<Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        return "admin/productos/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("unidades", unidadMedidaService.findAll());
        return "admin/productos/form";
    }

    @PostMapping("/save")
    public String saveProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        productoService.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("message", "Producto guardado exitosamente!");
        return "redirect:/admin/productos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("proveedores", proveedorService.findAll());
            model.addAttribute("unidades", unidadMedidaService.findAll());
            return "admin/productos/form";
        } else {
            return "redirect:/admin/productos";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productoService.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("message", "Producto eliminado exitosamente!");
        return "redirect:/admin/productos";
    }
}
