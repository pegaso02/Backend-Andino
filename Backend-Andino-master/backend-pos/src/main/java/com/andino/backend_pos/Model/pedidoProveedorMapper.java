package com.andino.backend_pos.Model;

import com.andino.backend_pos.DTOs.DetallePedidoRequestDTO;
import com.andino.backend_pos.DTOs.PedidoProveedorRequestDTO;
import com.andino.backend_pos.Repository.ProductoRepository;
import com.andino.backend_pos.Repository.ProveedorRepository;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class pedidoProveedorMapper {

    @Service

    public class PedidoProveedorMapper {

        private final ProveedorRepository proveedorRepository;
        private final ProductoRepository productoRepository;
        private final UnidadMedidaRepository unidadMedidaRepository;

        private PedidoProveedorMapper(ProveedorRepository proveedorRepository, ProductoRepository productoRepository, UnidadMedidaRepository unidadMedidaRepository) {
            this.proveedorRepository = proveedorRepository;
            this.productoRepository = productoRepository;
            this.unidadMedidaRepository = unidadMedidaRepository;
        }

        public PedidoProveedor toEntity(PedidoProveedorRequestDTO dto) {
            PedidoProveedor pedido = new PedidoProveedor();
            pedido.setFecha(dto.getFecha());
            pedido.setProveedor(proveedorRepository.getReferenceById(dto.getProveedorId()));

            // Convertir cada detalle del DTO a la entidad
            List<DetallePedidoProveedor> detalles = dto.getDetalles().stream()
                    .map(det -> {
                        DetallePedidoProveedor detalle = new DetallePedidoProveedor();
                        detalle.setProducto(productoRepository.getReferenceById(det.getProductoId()));
                        detalle.setCantidad1(det.getCantidad());
                        detalle.setPrecioUnitario(det.getPrecioUnitario());
                        detalle.setUnidadMedida(unidadMedidaRepository.getReferenceById(det.getUnidadMedidaId()));
                        detalle.setPedido(pedido); // Relación bidireccional
                        return detalle;
                    })
                    .toList();

            pedido.setDetalles(detalles);
            return pedido;
        }
    }
}
