package com.restaurante.factory;

import com.restaurante.dao.*;
import com.restaurante.dao.impl.*;

/**
 * FACTORY METHOD: o Controller pede "me dê o DAO de Mesa/Cliente/Produto/Pedido"
 * sem precisar conhecer as classes concretas (MesaDAOImpl, etc.). Se amanhã
 * trocarmos JDBC por outra tecnologia de persistência, só esta fábrica muda.
 */
public class DAOFactory {

    public static MesaDAO criarMesaDAO() {
        return new MesaDAOImpl();
    }

    public static ClienteDAO criarClienteDAO() {
        return new ClienteDAOImpl();
    }

    public static ProdutoDAO criarProdutoDAO() {
        return new ProdutoDAOImpl();
    }

    public static PedidoDAO criarPedidoDAO() {
        return new PedidoDAOImpl();
    }
}
