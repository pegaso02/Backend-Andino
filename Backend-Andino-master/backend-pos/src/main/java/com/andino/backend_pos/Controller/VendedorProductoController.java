package com.andino.backend_pos.Controller;

import com.andino.backend_pos.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/vendedor/productos")
public class VendedorProductoController {

    private final ProductoService productoService;

    public VendedorProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listProductos(Model model) {
        List<com.andino.backend_pos.Model.Producto> productos = productoService.listarProductos();
        model.addAttribute("productos", productos);
        return "vendedor/productos/list";
    }
}
