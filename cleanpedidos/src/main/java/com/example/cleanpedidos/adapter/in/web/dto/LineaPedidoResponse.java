package com.example.cleanpedidos.adapter.in.web.dto;

import java.math.BigDecimal;

public record LineaPedidoResponse(
        String productoNombre,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
