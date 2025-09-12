package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.DetalleVenta;
import com.andino.backend_pos.Repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService{

    DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository){
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<DetalleVenta> findAll() {
        return detalleVentaRepository.findAllWithDetails();
    }

    @Override
    public List<DetalleVenta> findAllOrderByVentaIdAsc() {
        return detalleVentaRepository.findAllByOrderByVentaIdAsc();
    }
}
