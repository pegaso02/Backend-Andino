package com.andino.backend_pos.Controller;


import com.andino.backend_pos.DTOs.DetallePedidoRequestDTO;
import com.andino.backend_pos.DTOs.PedidoProveedorRequestDTO;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Service.PedidoService;
import com.andino.backend_pos.Service.ProductoService;
import com.andino.backend_pos.Service.ProveedorService;
import com.andino.backend_pos.Service.UnidadMedidaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidoProveedorController {

    private final PedidoService pedidoService;
    private final ProveedorService proveedorService;
    private final ProductoService productoService;
    private final UnidadMedidaService unidadMedidaService;

    public AdminPedidoProveedorController(PedidoService pedidoService, ProveedorService proveedorService, ProductoService productoService, UnidadMedidaService unidadMedidaService) {
        this.pedidoService = pedidoService;
        this.proveedorService = proveedorService;
        this.productoService = productoService;
        this.unidadMedidaService = unidadMedidaService;
    }

    @GetMapping
    public String listPedidos(Model model) {
        try {
            model.addAttribute("pedidos", pedidoService.listarTodos());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar los pedidos: " + e.getMessage());
            model.addAttribute("pedidos", new java.util.ArrayList<>()); // Provide empty list to avoid null pointer
        }
        return "admin/pedidos/list";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("pedido", new PedidoProveedor());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("unidades", unidadMedidaService.findAll());
        return "admin/pedidos/form";
    }

    @PostMapping("/save")
    public String savePedido(@ModelAttribute PedidoProveedor pedido, RedirectAttributes redirectAttributes) {

        PedidoProveedorRequestDTO requestDTO = new PedidoProveedorRequestDTO();
        requestDTO.setFecha(pedido.getFecha() != null ? pedido.getFecha() : LocalDate.now());
        requestDTO.setProveedorId(pedido.getProveedor().getId());

        List<DetallePedidoRequestDTO> detallesDTO = new ArrayList<>();
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (com.andino.backend_pos.Model.DetallePedidoProveedor detalle : pedido.getDetalles()) {
                if (detalle.getProducto() != null && detalle.getCantidad1() != null && detalle.getPrecioUnitario() != null) {
                    DetallePedidoRequestDTO detalleDTO = new DetallePedidoRequestDTO();
                    detalleDTO.setProductoId(detalle.getProducto().getId());
                    detalleDTO.setCantidad(detalle.getCantidad1());
                    detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                    detallesDTO.add(detalleDTO);
                }
            }
        }
        requestDTO.setDetalles(detallesDTO);

        pedidoService.crearPedido(requestDTO);
        redirectAttributes.addFlashAttribute("message", "Pedido guardado exitosamente!");
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<PedidoProveedor> pedido = pedidoService.getPedidoProveedorById(id);
        if (pedido.isPresent()) {
            model.addAttribute("pedido", pedido.get());
            model.addAttribute("proveedores", proveedorService.findAll());
            model.addAttribute("productos", productoService.listarProductos());
            model.addAttribute("unidades", unidadMedidaService.findAll());
            return "admin/pedidos/form";
        } else {
            return "redirect:/admin/pedidos";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePedido(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pedidoService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Pedido eliminado exitosamente!");
        return "redirect:/admin/pedidos";
    }
}
