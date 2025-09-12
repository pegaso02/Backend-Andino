package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.PagoProveedorRequestDTO;
import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Repository.CompraRepository;
import com.andino.backend_pos.Repository.DetalleCompraRepository;
import com.andino.backend_pos.Repository.PedidoProveedorRepository;
import com.andino.backend_pos.Repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final PedidoProveedorRepository pedidoProveedorRepository;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final PagoProveedorService pagoProveedorService;

    public CompraServiceImpl(CompraRepository compraRepository, PedidoProveedorRepository pedidoProveedorRepository, ProductoRepository productoRepository, DetalleCompraRepository detalleCompraRepository, PagoProveedorService pagoProveedorService) {
        this.compraRepository = compraRepository;
        this.pedidoProveedorRepository = pedidoProveedorRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.pagoProveedorService = pagoProveedorService;
    }

    @Override
    @Transactional
    public Compra registrarCompra(Long pedidoId, Compra compra, List<DetalleCompra> detalles) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        compra.setPedidoProveedor(pedido);
        compra.setProveedor(pedido.getProveedor());
        compra.setEstado("FINALIZADA");
        compra.setPagada(true);
        compra.setFecha(LocalDate.now());

        Compra nuevaCompra = compraRepository.save(compra);

        double subtotalTotal = 0.0;
        double ivaTotal = 0.0;

        for (DetalleCompra detalle : detalles) {
            detalle.setCompra(nuevaCompra);

            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            double iva = subtotal * 0.19;
            double total = subtotal + iva;

            detalle.setSubtotal(subtotal);
            detalle.setIva(iva);
            detalle.setTotal(total);

            subtotalTotal += subtotal;
            ivaTotal += iva;

            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);

            detalleCompraRepository.save(detalle);
        }

        // Establecer totales en la compra
        nuevaCompra.setSubtotal(subtotalTotal);
        nuevaCompra.setIva(ivaTotal);
        nuevaCompra.setTotal(subtotalTotal + ivaTotal);
        nuevaCompra.setDetalles(detalles);
        compraRepository.save(nuevaCompra);

        // Actualizar estado del pedido
        pedido.setEstado("RECIBIDO");
        pedidoProveedorRepository.save(pedido);

        // Registrar el pago
        PagoProveedorRequestDTO pagoDTO = new PagoProveedorRequestDTO();
        pagoDTO.setProveedorId(pedido.getProveedor().getId());
        pagoDTO.setMonto(nuevaCompra.getTotal());
        pagoDTO.setFecha(LocalDate.now());

        pagoProveedorService.registrarPago(pagoDTO);

        // Confirmar pago del pedido
        pedido.setEstado("PAGADO");
        pedidoProveedorRepository.save(pedido);

        return nuevaCompra;
    }





    @Override
    public List<Compra> findAll() {
        return compraRepository.findAllWithDetails();
    }

    @Override
    public Optional<Compra> findById(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        compraRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Compra updateEstado(Long id, String nuevoEstado) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        compra.setEstado(nuevoEstado);
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public Compra marcarComoPagada(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        compra.setPagada(true);
        return compraRepository.save(compra);
    }
}