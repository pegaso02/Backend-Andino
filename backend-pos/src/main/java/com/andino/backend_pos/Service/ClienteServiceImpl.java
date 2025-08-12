package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Cliente;
import com.andino.backend_pos.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    public final ClienteRepository clienteRepository;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> buscarCliente() {
        if (clienteRepository.count() == 0) {
            return null;
        }
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        if(clienteRepository.findById(id).isEmpty()){
            return null;
        }
        return clienteRepository.findById(id).get();
    }

    @Override
    public Cliente actualizarCliente(Long id,Cliente cliente) {
        return clienteRepository.findById(id)
                .map(p -> {
                    p.setNombre(cliente.getNombre());
                    p.setCorreo(cliente.getCorreo());
                    p.setDocumento(cliente.getDocumento());
                    p.setDireccion(cliente.getDireccion());
                    p.setTelefono(cliente.getTelefono());
                    return clienteRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

}
