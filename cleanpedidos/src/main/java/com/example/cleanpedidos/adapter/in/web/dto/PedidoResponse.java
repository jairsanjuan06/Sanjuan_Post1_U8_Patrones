package com.example.cleanpedidos.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoResponse(
        String pedidoId,
        String clienteNombre,
        String estado,
        List<LineaPedidoResponse> lineas,
        BigDecimal total
) {
}
