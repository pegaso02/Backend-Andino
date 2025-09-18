package com.andino.backend_pos.Controller;


import com.andino.backend_pos.DTOs.CompraRequestDTO;
import com.andino.backend_pos.DTOs.DetalleCompraRequest;
import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Repository.CompraRepository;
import com.andino.backend_pos.Repository.DetalleCompraRepository;
import com.andino.backend_pos.Service.CompraService;
import com.andino.backend_pos.Service.PedidoService;
import com.andino.backend_pos.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final DetalleCompraRepository detalleCompraRepository;
    private final CompraRepository compraRepository;

    public AdminCompraController(CompraService compraService, PedidoService pedidoService, ProductoService productoService, DetalleCompraRepository detalleCompraRepository, CompraRepository compraRepository) {
        this.compraService = compraService;
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.detalleCompraRepository = detalleCompraRepository;
        this.compraRepository = compraRepository;
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

    @GetMapping("/detalles")
    public String listDetalleCompras(Model model) {
        try {
            List<DetalleCompra> detalleCompras = detalleCompraRepository.findAll();
            detalleCompras.sort(Comparator.comparing(d -> d.getCompra().getId()));
            Map<Compra, List<DetalleCompra>> comprasAgrupadas = detalleCompras.stream()
                    .collect(Collectors.groupingBy(DetalleCompra::getCompra, LinkedHashMap::new, Collectors.toList()));
            model.addAttribute("comprasAgrupadas", comprasAgrupadas);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al listar los detalles de compras: " + e.getMessage());
            model.addAttribute("comprasAgrupadas", new java.util.HashMap<>());
        }
        return "admin/compras/detalles/list-all";
    }

    @GetMapping("/detalles/{id}")
    public String viewCompraDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Compra compra = compraService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + id));
            List<DetalleCompra> detalles = detalleCompraRepository.findByCompra(compra);
            System.out.println(detalles);
            model.addAttribute("compra", compra);
            model.addAttribute("detalles", detalles);
            return "admin/compras/detalles/list";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/compras";
        }
    }

    @GetMapping("/descargar-csv")
    public ResponseEntity<byte[]> descargarCSV() {
        List<Compra> compras = compraService.findAll();
        StringWriter stringWriter = new StringWriter();

        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {
                    "Compra_ID",
                    "Detalle_ID",
                    "Fecha",
                    "Cantidad",
                    "Subtotal Producto",
                    "IVA",
                    "Total",
                    "Estado",
                    "Pagada",
                    "Proveedor",
                    "Pedido ID",
                    "Producto"
            };
            csvWriter.writeNext(header);

            Long compraAnteriorId = null;

            for (Compra compra : compras) {
                List<DetalleCompra> detalles = compra.getDetalles();
                boolean mostrarTotales = !compra.getId().equals(compraAnteriorId);

                for (DetalleCompra detalle : detalles) {
                    String[] data = {
                            String.valueOf(compra.getId()),                          // Compra_ID
                            String.valueOf(detalle.getId()),                         // Detalle_ID
                            String.valueOf(compra.getFecha()),                       // Fecha
                            String.valueOf(detalle.getCantidad()),
                            String.valueOf(detalle.getPrecioUnitario() * detalle.getCantidad()), // Subtotal Producto
                            mostrarTotales ? String.valueOf(compra.getIva()) : "",  // IVA
                            mostrarTotales ? String.valueOf(compra.getTotal()) : "",// Total
                            compra.getEstado(),
                            compra.isPagada() ? "Si" : "No",
                            compra.getProveedor() != null ? compra.getProveedor().getNombre() : "N/A",
                            compra.getPedidoProveedor() != null ? String.valueOf(compra.getPedidoProveedor().getId()) : "N/A",
                            detalle.getProducto().getNombre()                        // Producto
                    };

                    csvWriter.writeNext(data);
                    compraAnteriorId = compra.getId(); // actualizar ID
                    mostrarTotales = false; // solo mostrar en la primera fila de esa compra
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String csv = stringWriter.toString();
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

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
