package com.restaurante.dao;

import com.restaurante.model.Pedido;

public interface PedidoDAO extends GenericDAO<Pedido, Integer> {
    Pedido consultarPedidoAbertoPorMesa(int mesaId);
    void adicionarItem(Pedido pedido, com.restaurante.model.ItemPedido item);
}
