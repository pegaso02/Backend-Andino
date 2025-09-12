package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClienteService {

    Cliente guardarCliente(Cliente cliente);
    List<Cliente> buscarCliente();
    Cliente buscarClientePorId(Long id);
    Cliente actualizarCliente(Long id,Cliente cliente);
    void eliminarCliente(Long id);
}
