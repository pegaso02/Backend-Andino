package com.andino.backend_pos.Repository;

import com.andino.backend_pos.Model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
