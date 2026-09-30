package com.restaurante.builder;

import com.restaurante.model.*;

import java.time.LocalDateTime;

/**
 * BUILDER: Pedido tem vários atributos (mesa, cliente, garçom, itens, status,
 * datas, valores, forma de pagamento...). O Builder monta o objeto passo a
 * passo de forma legível, sem construtores telescópicos.
 */
public class PedidoBuilder {

    private final Pedido pedido = new Pedido();

    public PedidoBuilder paraMesa(Mesa mesa) {
        pedido.setMesa(mesa);
        return this;
    }

    public PedidoBuilder doCliente(Cliente cliente) {
        pedido.setCliente(cliente);
        return this;
    }

    public PedidoBuilder atendidoPor(String garcom) {
        pedido.setGarcom(garcom);
        return this;
    }

    public PedidoBuilder comObservacao(String observacao) {
        pedido.setObservacao(observacao);
        return this;
    }

    public Pedido abrir() {
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setDataAbertura(LocalDateTime.now());
        return pedido;
    }
}
