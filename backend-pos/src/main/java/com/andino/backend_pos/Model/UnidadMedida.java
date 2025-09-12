package com.andino.backend_pos.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UnidadMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;      // Ej: Kilogramo, Unidad, Litro
    private String simbolo;     // Ej: kg, un, L

    private boolean permiteDecimales = true;

    public boolean isPermiteDecimales() {
        return permiteDecimales;
    }

    public void setPermiteDecimales(boolean permiteDecimales) {
        this.permiteDecimales = permiteDecimales;
    }

    public BigDecimal getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(BigDecimal factorConversion) {
        this.factorConversion = factorConversion;
    }

    // Factor de conversión opcional (ej: si base es kg, lb = 0.453592)
    @Column(precision = 10, scale = 6)
    private BigDecimal factorConversion = BigDecimal.ONE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}
