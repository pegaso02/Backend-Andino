package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.DetalleVentaRequestDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.*;
import com.andino.backend_pos.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    DetalleVentaRepository detalleVentaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, ProductoRepository productoRepository, ClienteRepository clienteRepository, UsuarioRepository usuarioRepository,DetalleVentaRepository detalleVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional
    public Venta registrarVenta(VentaRequestDTO request) {


        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setFecha(LocalDate.now());

        venta.setUsuario(usuario);

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;

        Venta nuevaVenta = ventaRepository.save(venta);

        for (DetalleVentaRequestDTO d : request.getDetalles()) {
            Producto producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));



           try {
               // Convertir cantidad según la unidad del producto
               BigDecimal factorConversion = producto.getUnidadMedida().getFactorConversion() != null
                       ? producto.getUnidadMedida().getFactorConversion()
                       : BigDecimal.ONE;

               BigDecimal cantidadConvertida = d.getCantidad().multiply(factorConversion);
               BigDecimal stockActual = BigDecimal.valueOf(producto.getStock());
               if (stockActual.compareTo(cantidadConvertida) < 0) {
                   throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());

               }

               // Descontar stock
               producto.setStock(stockActual.subtract(cantidadConvertida).intValue());
               productoRepository.save(producto);

               // Calcular subtotal = cantidad * precio
               // Precio desde BD
               BigDecimal precio = producto.getPrecioVenta();

               // Subtotal
               BigDecimal subtotal = precio.multiply(cantidadConvertida);

               // Acumular total
               total = total.add(subtotal);


               // Guardar detalle
               DetalleVenta detalle = new DetalleVenta();
               detalle.setVenta(nuevaVenta);
               detalle.setProducto(producto);
               detalle.setCantidad(d.getCantidad());
               detalle.setPrecioUnitario(precio);
               detalle.setSubtotal(subtotal);

               detalleVentaRepository.save(detalle);
           }catch (RuntimeException e) {
               e.printStackTrace();
           }


        }

        iva = total.multiply(new BigDecimal("0.19")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalConIva = total.add(iva).setScale(2, RoundingMode.HALF_UP);

        nuevaVenta.setIva(iva);
        nuevaVenta.setTotal(totalConIva);

        return ventaRepository.save(nuevaVenta);
    }

    @Override
    public FacturaVentaDTO generarFactura(Venta venta) {
        // This method would typically generate a more complex DTO for a full invoice
        // For now, just a basic mapping
        FacturaVentaDTO factura = new FacturaVentaDTO();
        factura.setVentaId(venta.getId());
        factura.setFecha(venta.getFecha());
        factura.setTotal(venta.getTotal());
        return factura;
    }

    @Override
    public List<Venta> findAll() {
        return ventaRepository.findAllWithDetails();
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }
}
