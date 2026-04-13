package com.example.cleanpedidos.adapter.out.persistence;

import com.example.cleanpedidos.domain.entity.Pedido;
import com.example.cleanpedidos.domain.valueobject.Dinero;
import com.example.cleanpedidos.domain.valueobject.EstadoPedido;
import com.example.cleanpedidos.domain.valueobject.PedidoId;
import com.example.cleanpedidos.usecase.port.PedidoRepositoryPort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository jpa;
    private final ObjectMapper objectMapper;

    public PedidoRepositoryAdapter(PedidoJpaRepository jpa, ObjectMapper objectMapper) {
        this.jpa = jpa;
        this.objectMapper = objectMapper;
    }

    @Override
    public void guardar(Pedido pedido) {
        jpa.save(toEntity(pedido));
    }

    @Override
    public Optional<Pedido> buscarPorId(PedidoId id) {
        return jpa.findById(id.toString()).map(this::toDomain);
    }

    @Override
    public List<Pedido> buscarTodos() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    private Pedido toDomain(PedidoJpaEntity entity) {
        Pedido pedido = new Pedido(
                new PedidoId(UUID.fromString(entity.getId())),
                entity.getClienteNombre()
        );

        List<Map<String, Object>> lineas = readLineas(entity.getLineasJson());
        lineas.forEach(l -> pedido.agregarLinea(
                (String) l.get("productoNombre"),
                (Integer) l.get("cantidad"),
                new Dinero(new java.math.BigDecimal(l.get("precioUnitario").toString()))
        ));

        if (EstadoPedido.CONFIRMADO.name().equals(entity.getEstado())) {
            pedido.confirmar();
        }

        return pedido;
    }

    private PedidoJpaEntity toEntity(Pedido pedido) {
        return new PedidoJpaEntity(
                pedido.getId().toString(),
                pedido.getClienteNombre(),
                writeLineas(pedido),
                pedido.getEstado().name(),
                pedido.calcularTotal().cantidad()
        );
    }

 private String writeLineas(Pedido pedido) {
    List<Map<String, Object>> lineas = pedido.getLineas().stream()
            .map(l -> Map.<String, Object>of(
                    "productoNombre", l.productoNombre(),
                    "cantidad", l.cantidad(),
                    "precioUnitario", l.precioUnitario().cantidad()
            ))
            .toList();

    try {
        return objectMapper.writeValueAsString(lineas);
    } catch (JsonProcessingException e) {
        throw new IllegalStateException("No fue posible serializar las lineas del pedido", e);
    }
}


    private List<Map<String, Object>> readLineas(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No fue posible deserializar las lineas del pedido", e);
        }
    }
}
