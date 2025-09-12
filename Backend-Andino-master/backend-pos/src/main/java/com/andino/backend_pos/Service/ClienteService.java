package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> findAll();
}