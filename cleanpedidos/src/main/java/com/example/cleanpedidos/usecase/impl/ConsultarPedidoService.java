package com.example.cleanpedidos.usecase.impl;

import com.example.cleanpedidos.adapter.in.web.dto.LineaPedidoResponse;
import com.example.cleanpedidos.adapter.in.web.dto.PedidoResponse;
import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.ConsultarPedidoUseCase;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;

import java.util.List;

public class ConsultarPedidoService implements ConsultarPedidoUseCase {

    private final PedidoRepositoryPort repo;

    public ConsultarPedidoService(PedidoRepositoryPort repo) {
        this.repo = repo;
    }

    @Override
    public PedidoResponse buscarPorId(PedidoId id) {
        Pedido pedido = repo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido " + id + " no encontrado"));
        return toResponse(pedido);
    }

    @Override
    public List<PedidoResponse> listarTodos() {
        return repo.buscarTodos().stream()
                .map(this::toResponse)
                .toList();
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<LineaPedidoResponse> lineas = pedido.getLineas().stream()
                .map(l -> new LineaPedidoResponse(
                        l.productoNombre(),
                        l.cantidad(),
                        l.precioUnitario().cantidad(),
                        l.subtotal().cantidad()))
                .toList();

        return new PedidoResponse(
                pedido.getId().toString(),
                pedido.getClienteNombre(),
                pedido.getEstado().name(),
                lineas,
                pedido.calcularTotal().cantidad());
    }
}
