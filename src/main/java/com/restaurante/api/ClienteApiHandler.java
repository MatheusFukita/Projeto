package com.restaurante.api;

import com.restaurante.api.dto.CriarClienteRequest;
import com.restaurante.controller.RestauranteController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Rotas:
 *  GET  /api/clientes -> lista clientes
 *  POST /api/clientes -> cadastra cliente {nome, telefone, cpf}
 */
public class ClienteApiHandler implements HttpHandler {

    private final RestauranteController controller;

    public ClienteApiHandler(RestauranteController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (JsonUtil.tratarPreflight(exchange)) return;
        try {
            String metodo = exchange.getRequestMethod();
            String[] partes = exchange.getRequestURI().getPath().split("/");

            if ("GET".equalsIgnoreCase(metodo) && partes.length == 3) {
                JsonUtil.responderJson(exchange, 200, controller.listarClientes());

            } else if ("POST".equalsIgnoreCase(metodo) && partes.length == 3) {
                CriarClienteRequest req = JsonUtil.lerCorpo(exchange, CriarClienteRequest.class);
                controller.cadastrarCliente(req.nome, req.telefone, req.cpf);
                JsonUtil.responderJson(exchange, 201, controller.listarClientes());

            } else {
                JsonUtil.responderErro(exchange, 404, "Rota não encontrada");
            }
        } catch (Exception e) {
            JsonUtil.responderErro(exchange, 400, e.getMessage());
        }
    }
}
