package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.UnidadMedida;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepository;

    public UnidadMedidaServiceImpl(UnidadMedidaRepository unidadMedidaRepository) {
        this.unidadMedidaRepository = unidadMedidaRepository;
    }

    @Override
    public List<UnidadMedida> findAll() {
        return unidadMedidaRepository.findAll();
    }
}
