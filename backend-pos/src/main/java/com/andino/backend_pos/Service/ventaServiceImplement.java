package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.DetalleVentaRequestDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.FacturaVentaDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.LineaFacturaDTO;
import com.andino.backend_pos.DTOs.VentaRequestDTO;
import com.andino.backend_pos.Model.DetalleVenta;
import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Model.Usuario;
import com.andino.backend_pos.Model.Venta;
import com.andino.backend_pos.Repository.DetalleVentaRepository;
import com.andino.backend_pos.Repository.ProductoRepository;
import com.andino.backend_pos.Repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ventaServiceImplement implements VentaService {

    ProductoRepository productoRepository;
    DetalleVentaRepository detalleVentaRepository;

    VentaRepository ventaRepository;

    public ventaServiceImplement(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository,
                                 ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
    }


    @Override
    public Venta registrarVenta(VentaRequestDTO request) {
        Venta venta = new Venta();
        venta.setFecha(request.getFecha());

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;

        Venta nuevaVenta = ventaRepository.save(venta);

        for (DetalleVentaRequestDTO d : request.getDetalles()) {
            Producto producto = productoRepository.findById(d.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));



            // Convertir cantidad según la unidad del producto
            BigDecimal factorConversion = producto.getUnidadMedida().getFactorConversion() != null
                    ? producto.getUnidadMedida().getFactorConversion()
                    : BigDecimal.ONE;

            BigDecimal cantidadConvertida = d.getCantidad().multiply(factorConversion);



            // Validar stock
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
        }

        iva = total.multiply(new BigDecimal("0.19")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalConIva = total.add(iva).setScale(2, RoundingMode.HALF_UP);

        nuevaVenta.setIva(iva);
        nuevaVenta.setTotal(totalConIva);

        return ventaRepository.save(nuevaVenta);
    }


    public FacturaVentaDTO generarFactura(Venta venta){
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(venta.getId());

        List<LineaFacturaDTO> lineas = detalles.stream().map(det -> {
            LineaFacturaDTO linea = new LineaFacturaDTO();
            linea.setProducto(det.getProducto().getNombre());
            linea.setCantidad(det.getCantidad());
            linea.setPrecioUnitario(det.getPrecioUnitario());
            linea.setSubtotal(det.getSubtotal());
            return linea;
        }).collect(Collectors.toList());
        FacturaVentaDTO factura = new FacturaVentaDTO();
        factura.setVentaId(venta.getId());
        factura.setFecha(venta.getFecha());
        factura.setDetalles(lineas);
        factura.setSubtotal(venta.getTotal().subtract(venta.getIva()));
        factura.setIva(venta.getIva());
        factura.setTotal(venta.getTotal());

        return factura;
    }



}