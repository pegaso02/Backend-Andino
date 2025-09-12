package com.andino.backend_pos.Service;

import com.andino.backend_pos.Model.Producto;
import com.andino.backend_pos.Repository.CategoriaRepository;
import com.andino.backend_pos.Repository.ProductoRepository;
import com.andino.backend_pos.Repository.ProveedorRepository;
import com.andino.backend_pos.Repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;


    private final CategoriaRepository categoriaRepository;


    private final ProveedorRepository proveedorRepository;


    private final  UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, ProveedorRepository proveedorRepository, UnidadMedidaRepository unidadMedidaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.unidadMedidaRepository = unidadMedidaRepository;

    }


    @Override
    public Producto guardarProducto(Producto producto) {
        // Cargamos las referencias ya existentes (sin hacer SELECT completo)
        var cat = categoriaRepository.getReferenceById(producto.getCategoria().getId());
        var prov = proveedorRepository.getReferenceById(producto.getProveedor().getId());
        var unidad = unidadMedidaRepository.getReferenceById(producto.getUnidadMedida().getId());

        producto.setCategoria(cat);
        producto.setProveedor(prov);
        producto.setUnidadMedida(unidad);

        return productoRepository.save(producto);
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(productoActualizado.getNombre());
                    p.setPrecioVenta(productoActualizado.getPrecioVenta());
                    p.setStock(productoActualizado.getStock());
                    p.setCategoria(productoActualizado.getCategoria());
                    p.setUnidadMedida(productoActualizado.getUnidadMedida());
                    return productoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    };

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    }



