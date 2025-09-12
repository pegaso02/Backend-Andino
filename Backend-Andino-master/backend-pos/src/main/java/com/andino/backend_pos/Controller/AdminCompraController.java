package com.andino.backend_pos.Controller;


import com.andino.backend_pos.DTOs.CompraRequestDTO;
import com.andino.backend_pos.DTOs.DetalleCompraRequest;
import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Service.CompraService;
import com.andino.backend_pos.Service.PedidoService;
import com.andino.backend_pos.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.opencsv.CSVWriter;
import java.io.StringWriter;

@Controller
@RequestMapping("/admin/compras")
public class AdminCompraController {

    private final CompraService compraService;
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public AdminCompraController(CompraService compraService, PedidoService pedidoService, ProductoService productoService) {
        this.compraService = compraService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping
    public String listCompras(Model model) {
        try {
            List<Compra> compras = compraService.findAll();
            model.addAttribute("compras", compras);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar las compras: " + e.getMessage());
            model.addAttribute("compras", new java.util.ArrayList<>()); // Provide empty list to avoid null pointer
        }
        return "admin/compras/list";
    }

    @GetMapping("/descargar-csv")
    public ResponseEntity<byte[]> descargarCSV() {
        List<Compra> compras = compraService.findAll();
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {"ID", "Fecha", "Total", "Estado", "Pagada", "Proveedor", "Pedido ID"};
            csvWriter.writeNext(header);
            for (Compra compra : compras) {
                String[] data = {
                        String.valueOf(compra.getId()),
                        String.valueOf(compra.getFecha()),
                        String.valueOf(compra.getTotal()),
                        compra.getEstado(),
                        compra.isPagada() ? "Sí" : "No",
                        compra.getProveedor() != null ? compra.getProveedor().getNombre() : "N/A",
                        compra.getPedidoProveedor() != null ? String.valueOf(compra.getPedidoProveedor().getId()) : "N/A"
                };
                csvWriter.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String csv = stringWriter.toString();
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compras.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("compra", new Compra());
        model.addAttribute("pedidosPendientes", pedidoService.findPendingPedidos());
        model.addAttribute("productos", productoService.listarProductos());
        return "admin/compras/form";
    }

    @PostMapping("/save")
    public String saveCompra(@ModelAttribute Compra compra,
                             @RequestParam("pedidoProveedorId") Long pedidoProveedorId,
                             RedirectAttributes redirectAttributes) {
        try {
            // Obtener el PedidoProveedor
            PedidoProveedor pedidoProveedor = pedidoService.getPedidoProveedorById(pedidoProveedorId)
                    .orElseThrow(() -> new RuntimeException("Pedido a Proveedor no encontrado"));

            // Convertir DetallePedidoProveedor a DetalleCompra
            List<DetalleCompra> detallesCompra = pedidoProveedor.getDetalles().stream().map(dp -> {
                DetalleCompra dc = new DetalleCompra();
                dc.setProducto(dp.getProducto());
                dc.setCantidad(dp.getCantidad1().intValue());
                dc.setPrecioUnitario(dp.getPrecioUnitario().doubleValue());

                // ❌ Ya no calculamos el subtotal aquí — lo hace el servicio
                return dc;
            }).collect(Collectors.toList());

            // Registrar la compra (el servicio calcula subtotal, iva y total)
            compraService.registrarCompra(pedidoProveedorId, compra, detallesCompra);

            // Cambiar estado del pedido
            pedidoService.cambiarEstado(pedidoProveedorId, "PAGADO");

            redirectAttributes.addFlashAttribute("message", "Compra registrada y pedido finalizado exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la compra: " + e.getMessage());
        }

        return "redirect:/admin/compras";
    }


    @GetMapping("/finalizar/{id}")
    public String finalizarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            compraService.updateEstado(id, "FINALIZADA");
            redirectAttributes.addFlashAttribute("message", "Compra finalizada exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/compras";
    }

    @GetMapping("/pagar/{id}")
    public String pagarCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            compraService.marcarComoPagada(id);
            redirectAttributes.addFlashAttribute("message", "Compra marcada como pagada exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/compras";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompra(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            compraService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Compra eliminada exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/compras";
    }
}
