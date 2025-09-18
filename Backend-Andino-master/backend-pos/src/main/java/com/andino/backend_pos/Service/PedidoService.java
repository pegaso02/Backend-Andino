package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.PedidoProveedorRequestDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.DetallePedidoResponseDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.PedidoProveedorResponseDTO;
import com.andino.backend_pos.Model.*;
import com.andino.backend_pos.Repository.PedidoProveedorRepository;
import com.andino.backend_pos.Repository.ProductoRepository;
import com.andino.backend_pos.Repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoProveedorRepository repo;
    private final ProveedorRepository provRepo;
    private final ProductoRepository prodRepo;

    public PedidoService(PedidoProveedorRepository repo, ProveedorRepository provRepo, ProductoRepository prodRepo) {
        this.repo = repo;
        this.provRepo = provRepo;
        this.prodRepo = prodRepo;

    }

    @Transactional
    public PedidoProveedorResponseDTO crearPedido(PedidoProveedorRequestDTO req) {
        Proveedor p = provRepo.findById(req.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no existe"));

        PedidoProveedor pedido = new PedidoProveedor();
        pedido.setFecha(req.getFecha());
        pedido.setEstado("PENDIENTE");
        pedido.setProveedor(p);

        for (var d : req.getDetalles()) {
            Producto prod = prodRepo.findById(d.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no existe"));

             UnidadMedida unidad =prod.getUnidadMedida();

            // Validación: si no permite decimales y cantidad tiene decimales
            if (!unidad.isPermiteDecimales() && d.getCantidad().scale() > 0 ) {
                throw new RuntimeException("La unidad '" + unidad.getNombre() + "' no permite decimales.");
            }

            // Conversión: aplicar factorConversion para convertir a unidad base
            BigDecimal cantidadConvertida = d.getCantidad().multiply(unidad.getFactorConversion());

            DetallePedidoProveedor det = new DetallePedidoProveedor();
            det.setPedido(pedido);
            det.setProducto(prod);
            det.setCantidad1(cantidadConvertida);
            det.setPrecioUnitario(d.getPrecioUnitario());

            pedido.getDetalles().add(det);
        }
        PedidoProveedor guardado = repo.save(pedido);
        return mapToResponse(guardado);
    }

    @Transactional
    public PedidoProveedorResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        PedidoProveedor pedido = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<String> estadosValidos = List.of("PENDIENTE", "ENVIADO", "RECIBIDO", "PAGADO", "FINALIZADO");

        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new IllegalArgumentException("Estado no válido");
        }

        pedido.setEstado(nuevoEstado.toUpperCase());
        PedidoProveedor actualizado = repo.save(pedido);

        return mapToResponse(actualizado);
    }

    private PedidoProveedorResponseDTO mapToResponse(PedidoProveedor p) {
        PedidoProveedorResponseDTO dto = new PedidoProveedorResponseDTO();
        dto.setId(p.getId());
        dto.setFecha(p.getFecha());
        dto.setEstado(p.getEstado());
        dto.setProveedorId(p.getProveedor().getId());
        dto.setDetalles(p.getDetalles().stream().map(dp -> {

            DetallePedidoResponseDTO dd = new DetallePedidoResponseDTO();
            dd.setProductoId(dp.getProducto().getId());
            dd.setCantidad(dp.getCantidad1());
            dd.setPrecioUnitario(dp.getPrecioUnitario());
            dd.setUnidadSimbolo(dp.getProducto().getUnidadMedida().getSimbolo());
            return dd;
        }).toList());
        return dto;
    }

    public PedidoProveedorResponseDTO obtenerPedido(Long id) {
        PedidoProveedor pedido = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return mapToResponse(pedido);
    }

    public List<PedidoProveedor> listarTodos() {
        return repo.findAllWithDetails();
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Optional<PedidoProveedor> getPedidoProveedorById(Long id) {
        return repo.findByIdWithDetails(id);
    }

    public List<PedidoProveedor> findPendingPedidos() {
        return repo.findByEstado("PENDIENTE");
    }

}
