package com.restaurante.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Centraliza a serialização JSON (Jackson) e os cabeçalhos CORS,
 * para que a página HTML (aberta localmente no navegador) consiga
 * chamar esta API rodando em outra porta/origem.
 */
public class JsonUtil {

    public static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static <T> T lerCorpo(HttpExchange exchange, Class<T> tipo) throws IOException {
        return MAPPER.readValue(exchange.getRequestBody(), tipo);
    }

    public static void adicionarCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    public static void responderJson(HttpExchange exchange, int status, Object corpo) throws IOException {
        byte[] bytes = MAPPER.writeValueAsBytes(corpo);
        adicionarCors(exchange);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void responderErro(HttpExchange exchange, int status, String mensagem) throws IOException {
        responderJson(exchange, status, Map.of("erro", mensagem));
    }

    public static boolean tratarPreflight(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            adicionarCors(exchange);
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    public static byte[] utf8(String texto) {
        return texto.getBytes(StandardCharsets.UTF_8);
    }
}
