package com.example.cleanpedidos.adapter.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")
public class PedidoJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String clienteNombre;

    @Lob
    @Column(nullable = false)
    private String lineasJson;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private BigDecimal total;

    public PedidoJpaEntity() {
    }

    public PedidoJpaEntity(String id, String clienteNombre, String lineasJson, String estado, BigDecimal total) {
        this.id = id;
        this.clienteNombre = clienteNombre;
        this.lineasJson = lineasJson;
        this.estado = estado;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getLineasJson() {
        return lineasJson;
    }

    public String getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public void setLineasJson(String lineasJson) {
        this.lineasJson = lineasJson;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
