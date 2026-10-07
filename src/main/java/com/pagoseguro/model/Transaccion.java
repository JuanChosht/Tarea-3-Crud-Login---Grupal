package com.pagoseguro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El producto es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String producto;

    @Size(max = 255, message = "Máximo 255 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.00", message = "El monto mínimo es $1.00")
    @DecimalMax(value = "10000.00", message = "El monto máximo es $10,000.00")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @NotBlank(message = "El nombre del vendedor es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String vendedor;

    @NotBlank(message = "El correo del comprador es obligatorio")
    @Email(message = "Correo no válido")
    @Size(max = 100, message = "Máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String emailComprador;

    @NotNull(message = "El plazo de entrega es obligatorio")
    @Min(value = 1, message = "Mínimo 1 día")
    @Max(value = 30, message = "Máximo 30 días")
    @Column(nullable = false)
    private Integer diasEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTransaccion estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Transaccion() {
    }

    public Transaccion(String producto, String descripcion, BigDecimal monto, String vendedor,
                       String emailComprador, Integer diasEntrega, EstadoTransaccion estado) {
        this.producto = producto;
        this.descripcion = descripcion;
        this.monto = monto;
        this.vendedor = vendedor;
        this.emailComprador = emailComprador;
        this.diasEntrega = diasEntrega;
        this.estado = estado;
    }

    @PrePersist
    void alCrear() {
        if (estado == null) {
            estado = EstadoTransaccion.CREADA;
        }
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    public String getCodigo() {
        return id == null ? "" : String.format("PS-%05d", id);
    }

    public boolean isModificable() {
        return estado == null || estado == EstadoTransaccion.CREADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getEmailComprador() {
        return emailComprador;
    }

    public void setEmailComprador(String emailComprador) {
        this.emailComprador = emailComprador;
    }

    public Integer getDiasEntrega() {
        return diasEntrega;
    }

    public void setDiasEntrega(Integer diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
