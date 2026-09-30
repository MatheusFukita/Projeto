package com.restaurante.api;

import com.restaurante.api.dto.CriarMesaRequest;
import com.restaurante.controller.RestauranteController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Rotas:
 *  GET    /api/mesas       -> lista todas as mesas
 *  POST   /api/mesas       -> cadastra uma mesa {numero, capacidade, localizacao}
 *  DELETE /api/mesas/{id}  -> remove uma mesa
 */
public class MesaApiHandler implements HttpHandler {

    private final RestauranteController controller;

    public MesaApiHandler(RestauranteController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (JsonUtil.tratarPreflight(exchange)) return;
        try {
            String metodo = exchange.getRequestMethod();
            String[] partes = exchange.getRequestURI().getPath().split("/");

            if ("GET".equalsIgnoreCase(metodo) && partes.length == 3) {
                JsonUtil.responderJson(exchange, 200, controller.listarMesas());

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 3) {
                CriarMesaRequest req = JsonUtil.lerCorpo(exchange, CriarMesaRequest.class);
                controller.cadastrarMesa(req.numero, req.capacidade, req.localizacao);
                JsonUtil.responderJson(exchange, 201, controller.listarMesas());

            } else if ("DELETE".equalsIgnoreCase(metodo) && partes.length == 4) {
                int id = Integer.parseInt(partes[3]);
                controller.removerMesa(id);
                JsonUtil.responderJson(exchange, 200, Map.of("ok", true));

            } else {
                JsonUtil.responderErro(exchange, 404, "Rota não encontrada");
            }
        } catch (Exception e) {
            JsonUtil.responderErro(exchange, 400, e.getMessage());
        }
    }
}
