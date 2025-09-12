package com.andino.backend_pos.DTOs.ResponseDTOs;

import java.time.LocalDate;
import java.util.List;

public class CompraResponse {
    private Long id;
    private LocalDate fecha;
    private Long proveedorId;
    private List<DetalleCompraResponseDTO> detalles;
    private Double total;
}
