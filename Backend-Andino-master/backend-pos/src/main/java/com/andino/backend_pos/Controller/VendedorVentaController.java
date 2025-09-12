package com.andino.backend_pos.Controller;

import com.andino.backend_pos.DTOs.DetalleVentaRequestDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.DetalleVenta;
import com.andino.backend_pos.Model.Venta;
import com.andino.backend_pos.Service.*;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.opencsv.CSVWriter;

import java.io.StringWriter;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vendedor/ventas")
public class VendedorVentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final DetalleVentaService detalleVentaService;

    public VendedorVentaController(VentaService ventaService, ClienteService clienteService, ProductoService productoService, UsuarioService usuarioService,DetalleVentaService detalleVentaService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping("/detalles")
    public String listDetalleVentas(Model model) {
        try {
            List<DetalleVenta> detalleVentas = detalleVentaService.findAllOrderByVentaIdAsc();
            Map<Venta, List<DetalleVenta>> ventasAgrupadas = detalleVentas.stream()
                    .collect(Collectors.groupingBy(DetalleVenta::getVenta, LinkedHashMap::new, Collectors.toList()));
            model.addAttribute("ventasAgrupadas", ventasAgrupadas);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al listar los detalles de ventas: " + e.getMessage());
            model.addAttribute("ventasAgrupadas", new HashMap<>());
        }
        return "vendedor/detalles/list";
    }

    @GetMapping
    public String listVentas(Model model) {
        try {
            model.addAttribute("ventas", ventaService.findAll());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar las ventas: " + e.getMessage());
            model.addAttribute("ventas", new java.util.ArrayList<>()); // Provide empty list to avoid null pointer
        }
        return "vendedor/ventas/list";
    }

    @GetMapping("/descargar-detalle-csv")
    public ResponseEntity<byte[]> descargarDetalleCSV() throws IOException {
        List<DetalleVenta> Detalleventas = detalleVentaService.findAllOrderByVentaIdAsc();
        StringWriter stringWriter = new StringWriter();

        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            // Encabezado del CSV
            String[] header = {"Venta_ID", "Detalle_ID", "Fecha", "SubTotal", "Total", "IVA", "Usuario", "Producto"};
            csvWriter.writeNext(header);

            Long ventaAnteriorId = null;

            for (DetalleVenta Detventa : Detalleventas) {
                Long ventaId = Detventa.getVenta().getId();
                boolean mostrarTotales = !ventaId.equals(ventaAnteriorId);

                String[] data = {
                        String.valueOf(ventaId),
                        String.valueOf(Detventa.getId()),
                        String.valueOf(Detventa.getVenta().getFecha()),
                        String.valueOf(Detventa.getSubtotal()),
                        mostrarTotales ? String.valueOf(Detventa.getVenta().getTotal()) : "",
                        mostrarTotales ? String.valueOf(Detventa.getVenta().getIva()) : "",
                        mostrarTotales ?
                                (Detventa.getVenta().getUsuario() != null ? Detventa.getVenta().getUsuario().getUsername() : "N/A")
                                : "",
                        Detventa.getProducto() != null ? Detventa.getProducto().getNombre() : "N/A"
                };

                csvWriter.writeNext(data);
                ventaAnteriorId = ventaId;
            }
        }

        String csv = stringWriter.toString();
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=detalle_ventas.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    @GetMapping("/descargar-csv")
    public ResponseEntity<byte[]> descargarCSV() {
        List<Venta> ventas = ventaService.findAll();
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {"ID", "Fecha", "Total", "IVA", "Cliente", "Usuario"};
            csvWriter.writeNext(header);
            for (Venta venta : ventas) {
                String[] data = {
                        String.valueOf(venta.getId()),
                        String.valueOf(venta.getFecha()),
                        String.valueOf(venta.getTotal()),
                        String.valueOf(venta.getIva()),
                        venta.getCliente() != null ? venta.getCliente().getNombre() : "N/A",
                        venta.getUsuario() != null ? venta.getUsuario().getUsername() : "N/A"
                };
                csvWriter.writeNext(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String csv = stringWriter.toString();
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ventas.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");
        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("productos", productoService.listarProductos());
        model.addAttribute("usuarios", usuarioService.findAll());
        return "vendedor/ventas/form";
    }

    @GetMapping("/factura/{id}")
    public String verFactura(@PathVariable Long id, Model model) {
        Optional<Venta> venta = ventaService.findById(id);
        if (venta.isPresent()) {
            model.addAttribute("venta", venta.get());
            return "vendedor/ventas/factura";
        } else {
            return "redirect:/vendedor/ventas";
        }
    }

    @GetMapping("/factura/pdf/{id}")
    public ResponseEntity<byte[]> descargarFacturaPDF(@PathVariable Long id) {
        Optional<Venta> ventaOptional = ventaService.findById(id);
        if (ventaOptional.isPresent()) {
            Venta venta = ventaOptional.get();
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document,stream);
                document.open();
                document.add(new Paragraph("Factura N°: "+venta.getId()));
                document.add(new Paragraph("Fecha: " + venta.getFecha()));
                document.add(new Paragraph("Cliente: " + (venta.getCliente() != null ? venta.getCliente().getNombre() : "N/A")));
                document.add(new Paragraph("Usuario: " + (venta.getUsuario() != null ? venta.getUsuario().getUsername() : "N/A")));
                document.add(new Paragraph("--------------------------------------------------"));
                for (com.andino.backend_pos.Model.DetalleVenta detalle : venta.getDetalles()) {
                    document.add(new Paragraph(
                            detalle.getProducto().getNombre() +
                                    " - Cantidad: " + detalle.getCantidad() +" "+detalle.getProducto().getUnidadMedida().getNombre()+
                                    " - Precio Unitario: " + detalle.getPrecioUnitario() +
                                    " - Subtotal: " + detalle.getSubtotal()
                    ));
                }
                document.add(new Paragraph("--------------------------------------------------"));
                document.add(new Paragraph("IVA: " + venta.getIva()));
                document.add(new Paragraph("Total: " + venta.getTotal()));
                document.close();

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + venta.getId() + ".pdf");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(stream.toByteArray());

            } catch (DocumentException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }


        }


    @PostMapping("/save")
    public String saveVenta(@ModelAttribute Venta venta, RedirectAttributes redirectAttributes) {
        try {
            VentaRequestDTO requestDTO = new VentaRequestDTO();

            requestDTO.setUsuarioId(venta.getUsuario().getId());

            List<DetalleVentaRequestDTO> detallesDTO = new ArrayList<>();
            if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
                for (com.andino.backend_pos.Model.DetalleVenta detalle : venta.getDetalles()) {
                    if (detalle.getProducto() != null && detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
                        DetalleVentaRequestDTO detalleDTO = new DetalleVentaRequestDTO();
                        detalleDTO.setProductoId(detalle.getProducto().getId());
                        detalleDTO.setCantidad(detalle.getCantidad());
                        detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                        detallesDTO.add(detalleDTO);
                    }
                }
            }
            requestDTO.setDetalles(detallesDTO);

            ventaService.registrarVenta(requestDTO);
            redirectAttributes.addFlashAttribute("message", "Venta registrada exitosamente!");
            return "redirect:/vendedor/ventas";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al registrar la venta: " + e.getMessage());
            return "redirect:/vendedor/ventas/new"; // Redirect back to the form with error
        }
    }
}
