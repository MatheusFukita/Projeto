package com.restaurante.api;

import com.restaurante.api.dto.AbrirPedidoRequest;
import com.restaurante.api.dto.AdicionarItemRequest;
import com.restaurante.api.dto.FecharPedidoRequest;
import com.restaurante.controller.RestauranteController;
import com.restaurante.model.FormaPagamento;
import com.restaurante.model.Pedido;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Rotas:
 *  GET  /api/pedidos             -> lista todos os pedidos
 *  GET  /api/pedidos/{id}        -> consulta um pedido
 *  POST /api/pedidos             -> abre pedido {mesaId, clienteId, garcom}
 *  POST /api/pedidos/{id}/itens  -> adiciona item {produtoId, quantidade, observacao}
 *  POST /api/pedidos/{id}/fechar -> fecha comanda {formaPagamento}  (automação de negócio)
 */
public class PedidoApiHandler implements HttpHandler {

    private final RestauranteController controller;

    public PedidoApiHandler(RestauranteController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (JsonUtil.tratarPreflight(exchange)) return;
        try {
            String metodo = exchange.getRequestMethod();
            String[] partes = exchange.getRequestURI().getPath().split("/");
            // partes: ["", "api", "pedidos"] | [...,"{id}"] | [...,"{id}","itens"] | [...,"{id}","fechar"]

            if ("GET".equalsIgnoreCase(metodo) && partes.length == 3) {
                JsonUtil.responderJson(exchange, 200, controller.listarPedidos());

            } else if ("GET".equalsIgnoreCase(metodo) && partes.length == 4) {
                Pedido pedido = controller.consultarPedido(Integer.parseInt(partes[3]));
                if (pedido == null) { JsonUtil.responderErro(exchange, 404, "Pedido não encontrado"); return; }
                JsonUtil.responderJson(exchange, 200, pedido);

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 3) {
                AbrirPedidoRequest req = JsonUtil.lerCorpo(exchange, AbrirPedidoRequest.class);
                Pedido pedido = controller.abrirPedido(req.mesaId, req.clienteId, req.garcom);
                JsonUtil.responderJson(exchange, 201, pedido);

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 5 && "itens".equals(partes[4])) {
                int pedidoId = Integer.parseInt(partes[3]);
                AdicionarItemRequest req = JsonUtil.lerCorpo(exchange, AdicionarItemRequest.class);
                controller.adicionarItem(pedidoId, req.produtoId, req.quantidade, req.observacao);
                JsonUtil.responderJson(exchange, 200, controller.consultarPedido(pedidoId));

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 5 && "fechar".equals(partes[4])) {
                int pedidoId = Integer.parseInt(partes[3]);
                FecharPedidoRequest req = JsonUtil.lerCorpo(exchange, FecharPedidoRequest.class);
                Pedido pedido = controller.fecharPedido(pedidoId, FormaPagamento.valueOf(req.formaPagamento));
                JsonUtil.responderJson(exchange, 200, pedido);

            } else {
                JsonUtil.responderErro(exchange, 404, "Rota não encontrada");
            }
        } catch (Exception e) {
            JsonUtil.responderErro(exchange, 400, e.getMessage());
        }
    }
}
