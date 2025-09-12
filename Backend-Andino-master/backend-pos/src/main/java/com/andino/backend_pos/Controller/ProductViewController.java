package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Service.CategoriaService;
import com.andino.backend_pos.Service.ProductoService;
import com.andino.backend_pos.Service.ProveedorService;
import com.andino.backend_pos.Service.UnidadMedidaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductViewController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;
    private final UnidadMedidaService unidadMedidaService;

    public ProductViewController(ProductoService productoService, CategoriaService categoriaService, ProveedorService proveedorService, UnidadMedidaService unidadMedidaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
        this.unidadMedidaService = unidadMedidaService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productoService.listarProductos());
        model.addAttribute("newProduct", new Producto());
        model.addAttribute("categories", categoriaService.findAll());
        model.addAttribute("providers", proveedorService.findAll());
        model.addAttribute("units", unidadMedidaService.findAll());
        return "products";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Producto newProduct) {
        productoService.guardarProducto(newProduct);
        return "redirect:/products";
    }
}
