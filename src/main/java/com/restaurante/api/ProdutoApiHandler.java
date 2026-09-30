package com.restaurante.api;

import com.restaurante.api.dto.CriarProdutoRequest;
import com.restaurante.controller.RestauranteController;
import com.restaurante.factory.DAOFactory;
import com.restaurante.model.Produto;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Rotas:
 *  GET   /api/produtos              -> lista todos os produtos
 *  POST  /api/produtos              -> cadastra produto
 *  PATCH /api/produtos/{id}/toggle  -> alterna disponibilidade
 */
public class ProdutoApiHandler implements HttpHandler {

    private final RestauranteController controller;

    public ProdutoApiHandler(RestauranteController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (JsonUtil.tratarPreflight(exchange)) return;
        try {
            String metodo = exchange.getRequestMethod();
            String[] partes = exchange.getRequestURI().getPath().split("/");

            if ("GET".equalsIgnoreCase(metodo) && partes.length == 3) {
                JsonUtil.responderJson(exchange, 200, controller.listarProdutos());

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 3) {
                CriarProdutoRequest req = JsonUtil.lerCorpo(exchange, CriarProdutoRequest.class);
                controller.cadastrarProduto(req.nome, req.categoria, req.preco, req.descricao, req.tempoPreparoMin);
                JsonUtil.responderJson(exchange, 201, controller.listarProdutos());

            } else if ("PATCH".equalsIgnoreCase(metodo) && partes.length == 5 && "toggle".equals(partes[4])) {
                int id = Integer.parseInt(partes[3]);
                Produto produto = DAOFactory.criarProdutoDAO().consultarPorId(id);
                if (produto == null) { JsonUtil.responderErro(exchange, 404, "Produto não encontrado"); return; }
                produto.setDisponivel(!produto.isDisponivel());
                DAOFactory.criarProdutoDAO().atualizar(produto);
                JsonUtil.responderJson(exchange, 200, controller.listarProdutos());

            } else {
                JsonUtil.responderErro(exchange, 404, "Rota não encontrada");
            }
        } catch (Exception e) {
            JsonUtil.responderErro(exchange, 400, e.getMessage());
        }
    }
}
