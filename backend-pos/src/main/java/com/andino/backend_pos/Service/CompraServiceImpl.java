package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.PagoProveedorRequestDTO;
import com.andino.backend_pos.Model.Compra;
import com.andino.backend_pos.Model.DetalleCompra;
import com.andino.backend_pos.Model.PedidoProveedor;
import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    public final CompraRepository compraRepository;
    public final ProductoRepository productoRepository;
    public final DetalleCompraRepository detalleCompraRepository;
    public final PagoProveedorService pagoProveedorService;
    private final PedidoProveedorRepository pedidoProveedorRepository;


    public CompraServiceImpl(CompraRepository compraRepository, ProductoRepository productoRepository, DetalleCompraRepository detalleCompraRepository, PagoProveedorService pagoProveedorService, PedidoProveedorRepository pedidoProveedorRepository){
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.pagoProveedorService = pagoProveedorService;


        this.pedidoProveedorRepository = pedidoProveedorRepository;
    }



    @Override
    public Compra registrarCompra(Long pedidoId, Compra compra, List<DetalleCompra> detalles) {

        PedidoProveedor pedido = pedidoProveedorRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));



        compra.setPedidoProveedor(pedido);
        compra.setProveedor(pedido.getProveedor());
        Compra nuevaCompra = compraRepository.save(compra);

        double total = 0.0;

        for (DetalleCompra detalle : detalles) {
            detalle.setCompra(nuevaCompra);
            double subtotal = detalle.getCantidad() * detalle.getPrecioUnitario();
            detalle.setSubtotal(subtotal);

            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);

            total += subtotal;
            detalleCompraRepository.save(detalle);
        }

        nuevaCompra.setTotal(total);
        nuevaCompra = compraRepository.save(nuevaCompra);

        // ✅ ACTUALIZAR ESTADO DEL PEDIDO A RECIBIDO
        pedido.setEstado("RECIBIDO");
        pedidoProveedorRepository.save(pedido);

        // ✅ AUTOMATIZAR PAGO
        PagoProveedorRequestDTO pagoDTO = new PagoProveedorRequestDTO();
        pagoDTO.setProveedorId(pedido.getProveedor().getId());
        pagoDTO.setMonto(total);
        pagoDTO.setFecha(LocalDate.now());

        pagoProveedorService.registrarPago(pagoDTO);

        // ✅ ACTUALIZAR ESTADO DEL PEDIDO A PAGADO
        pedido.setEstado("PAGADO");
        pedidoProveedorRepository.save(pedido);

        return nuevaCompra;
    }

}
