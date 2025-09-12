package com.andino.backend_pos.Service;

import com.andino.backend_pos.DTOs.PagoProveedorRequestDTO;
import com.andino.backend_pos.DTOs.ResponseDTOs.PagoProveedorResponseDTO;
import com.andino.backend_pos.Model.PagoProveedor;
import com.andino.backend_pos.Model.Proveedor;
import com.andino.backend_pos.Repository.PagoProveedorRepository;
import com.andino.backend_pos.Repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagoProveedorService {

    private final PagoProveedorRepository pagoRepo;
    private final ProveedorRepository proveedorRepo;

    public PagoProveedorService(PagoProveedorRepository pagoRepo, ProveedorRepository proveedorRepo) {
        this.pagoRepo = pagoRepo;
        this.proveedorRepo = proveedorRepo;
    }

    @Transactional
    public PagoProveedorResponseDTO registrarPago(PagoProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepo.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        PagoProveedor pago = new PagoProveedor();
        pago.setProveedor(proveedor);
        pago.setMonto(dto.getMonto());
        pago.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());

        PagoProveedor guardado = pagoRepo.save(pago);

        return mapToDTO(guardado);
    }

    public List<PagoProveedorResponseDTO> obtenerPagosPorProveedor(Long proveedorId) {
        List<PagoProveedor> pagos = pagoRepo.findByProveedorId(proveedorId);
        return pagos.stream().map(this::mapToDTO).toList();
    }

    private PagoProveedorResponseDTO mapToDTO(PagoProveedor pago) {
        PagoProveedorResponseDTO dto = new PagoProveedorResponseDTO();
        dto.setId(pago.getId());
        dto.setProveedorId(pago.getProveedor().getId());
        dto.setMonto(pago.getMonto());
        dto.setFecha(pago.getFecha());
        return dto;
    }


}
